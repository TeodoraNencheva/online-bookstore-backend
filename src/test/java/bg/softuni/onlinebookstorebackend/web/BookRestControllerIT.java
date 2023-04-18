package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.book.AddNewBookDTO;
import bg.softuni.onlinebookstorebackend.model.dto.book.BookDetailsDTO;
import bg.softuni.onlinebookstorebackend.model.dto.book.BookOverviewDTO;
import bg.softuni.onlinebookstorebackend.model.dto.search.SearchDTO;
import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import bg.softuni.onlinebookstorebackend.model.entity.BookEntity;
import bg.softuni.onlinebookstorebackend.model.entity.GenreEntity;
import bg.softuni.onlinebookstorebackend.model.entity.PictureEntity;
import bg.softuni.onlinebookstorebackend.model.error.AuthorNotFoundException;
import bg.softuni.onlinebookstorebackend.model.error.BookNotFoundException;
import bg.softuni.onlinebookstorebackend.model.error.GenreNotFoundException;
import bg.softuni.onlinebookstorebackend.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookRestControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private AuthorEntity testAuthor;
    private MockMultipartFile picture;
    private AddNewBookDTO bookDto;
    private ObjectMapper objectMapper;
    private MockMultipartFile book;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        testAuthor = new AuthorEntity();
        testAuthor.setFirstName("Ivan");
        testAuthor.setLastName("Vazov");
        testAuthor.setBiography("biography");

        picture = new MockMultipartFile("picture", "picture.png",
                "multipart/form-data", new byte[]{});
        bookDto = new AddNewBookDTO("Pod igoto", 1L, 1L, "1894",
                "some summary", new BigDecimal(20));

        objectMapper = new ObjectMapper();
        book = new MockMultipartFile("bookModel", "",
                "application/json", objectMapper.writeValueAsString(bookDto).getBytes());
    }

    @Test
    void canGetAllBooks() throws Exception {
        when(bookService.getAllBooks(any(Pageable.class)))
                .thenReturn(List.of(new BookOverviewDTO(1L, "Pod igoto", testAuthor, "novel", "picture")));

        mockMvc.perform(get("/api/books/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].title").value("Pod igoto"))
                .andExpect(jsonPath("$.[0].author.fullName").value("Ivan Vazov"))
                .andExpect(jsonPath("$.[0].genre").value("novel"))
                .andExpect(jsonPath("$.[0].picture").value("picture"));

        verify(bookService, times(1)).getAllBooks(any(Pageable.class));
    }

    @Test
    void canGetBooksByGenre() throws Exception {
        when(bookService.getBooksByGenre(anyString(), any(Pageable.class)))
                .thenReturn(List.of(new BookOverviewDTO(1L, "Pod igoto", testAuthor, "novel", "picture")));

        mockMvc.perform(get("/api/books/{genre}", "novel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].title").value("Pod igoto"))
                .andExpect(jsonPath("$.[0].author.fullName").value("Ivan Vazov"))
                .andExpect(jsonPath("$.[0].genre").value("novel"))
                .andExpect(jsonPath("$.[0].picture").value("picture"));

        verify(bookService, times(1)).getBooksByGenre(anyString(), any(Pageable.class));
    }

    @Test
    void cannotGetBookByGenreWhenGenreDoesNotExist() throws Exception {
        when(bookService.getBooksByGenre(anyString(), any(Pageable.class)))
                .thenThrow(new GenreNotFoundException("novel"));

        mockMvc.perform(get("/api/books/{genre}", "novel"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Genre novel not found"));

        verify(bookService, times(1)).getBooksByGenre(anyString(), any(Pageable.class));
    }

    @Test
    void canGetBookDetails() throws Exception {
        when(bookService.getBookDetails(anyLong()))
                .thenReturn(new BookDetailsDTO(1L, "Pod igoto", testAuthor, "novel",
                        "1894", "some summary", "some picture",
                        new BigDecimal(20)));

        mockMvc.perform(get("/api/books/{id}/details", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Pod igoto"))
                .andExpect(jsonPath("$.author.fullName").value("Ivan Vazov"))
                .andExpect(jsonPath("$.genre").value("novel"))
                .andExpect(jsonPath("$.yearOfPublication").value("1894"))
                .andExpect(jsonPath("$.summary").value("some summary"))
                .andExpect(jsonPath("$.picture").value("some picture"))
                .andExpect(jsonPath("$.price").value(20));

        verify(bookService, times(1)).getBookDetails(1L);
    }

    @Test
    void cannotGetBookDetailsWhenBookDoesNotExist() throws Exception {
        when(bookService.getBookDetails(anyLong()))
                .thenThrow(new BookNotFoundException(1L));

        mockMvc.perform(get("/api/books/{id}/details", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book with ID 1 not found"));

        verify(bookService, times(1)).getBookDetails(1L);
    }

    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    void adminCanAddNewBook() throws Exception {
        PictureEntity bookPicture = new PictureEntity();
        bookPicture.setUrl("picture url");
        BookEntity toReturn = new BookEntity(bookDto, testAuthor, new GenreEntity("novel"), bookPicture);

        when(bookService.addNewBook(any(AddNewBookDTO.class), any(MultipartFile.class)))
                .thenReturn(toReturn);

        mockMvc.perform(multipart("/api/books/add")
                        .file(book)
                        .file(picture))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Pod igoto"))
                .andExpect(jsonPath("$.author.fullName").value("Ivan Vazov"))
                .andExpect(jsonPath("$.genre.name").value("novel"))
                .andExpect(jsonPath("$.yearOfPublication").value("1894"))
                .andExpect(jsonPath("$.picture.url").value("picture url"))
                .andExpect(jsonPath("$.price").value("20"));

        verify(bookService, times(1)).addNewBook(any(AddNewBookDTO.class), any(MultipartFile.class));
    }

    @WithMockUser(authorities = "ROLE_USER")
    @Test
    void userCannotAddNewBook() throws Exception {
        mockMvc.perform(multipart("/api/books/add")
                        .file(book)
                        .file(picture))
                .andExpect(status().isForbidden());

        verify(bookService, never()).addNewBook(any(AddNewBookDTO.class));
    }

    @WithAnonymousUser
    @Test
    void anonymousUserCannotAddNewBook() throws Exception {
        mockMvc.perform(multipart("/api/books/add")
                        .file(book)
                        .file(picture))
                .andExpect(status().isForbidden());

        verify(bookService, never()).addNewBook(any(AddNewBookDTO.class));
    }

    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    void adminCanUpdateBook() throws Exception {
        PictureEntity bookPicture = new PictureEntity();
        bookPicture.setUrl("picture url");
        BookEntity toReturn = new BookEntity(bookDto, testAuthor, new GenreEntity("novel"), bookPicture);

        when(bookService.updateBook(any(AddNewBookDTO.class), anyLong(), any(MultipartFile.class)))
                .thenReturn(toReturn);

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/books/{id}", 1L)
                        .file(book)
                        .file(picture))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Pod igoto"))
                .andExpect(jsonPath("$.author.fullName").value("Ivan Vazov"))
                .andExpect(jsonPath("$.genre.name").value("novel"))
                .andExpect(jsonPath("$.yearOfPublication").value("1894"))
                .andExpect(jsonPath("$.summary").value("some summary"))
                .andExpect(jsonPath("$.price").value(20))
                .andExpect(jsonPath("$.picture.url").value("picture url"));

        verify(bookService, times(1)).updateBook(any(AddNewBookDTO.class), anyLong(), any(MultipartFile.class));
    }

    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    void adminCannotUpdateBookWhenBookDoesNotExist() throws Exception {
        when(bookService.updateBook(any(AddNewBookDTO.class), anyLong(), any(MultipartFile.class)))
                .thenThrow(new BookNotFoundException(1L));

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/books/{id}", 1L)
                        .file(book)
                        .file(picture))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book with ID 1 not found"));

        verify(bookService, times(1)).updateBook(any(AddNewBookDTO.class), anyLong(), any(MultipartFile.class));
    }

    @WithMockUser(authorities = "ROLE_USER")
    @Test
    void userCannotUpdateBook() throws Exception {
        mockMvc.perform(multipart(HttpMethod.PUT, "/api/books/{id}", 1L)
                        .file(book)
                        .file(picture))
                .andExpect(status().isForbidden());

        verify(bookService, never()).updateBook(any(AddNewBookDTO.class), anyLong());
    }

    @WithAnonymousUser
    @Test
    void anonymousUserCannotUpdateBook() throws Exception {
        mockMvc.perform(multipart(HttpMethod.PUT, "/api/books/{id}", 1L)
                        .file(book)
                        .file(picture))
                .andExpect(status().isForbidden());

        verify(bookService, never()).updateBook(any(AddNewBookDTO.class), anyLong());
    }

    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    void adminCanDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(anyLong());

        mockMvc.perform(delete("/api/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book with ID 1 deleted"));

        verify(bookService, times(1)).deleteBook(anyLong());
    }

    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    void adminCannotDeleteBookWhenBookDoesNotExist() throws Exception {
        doThrow(new BookNotFoundException(1L)).when(bookService).deleteBook(anyLong());

        mockMvc.perform(delete("/api/books/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book with ID 1 not found"));

        verify(bookService, times(1)).deleteBook(anyLong());
    }

    @WithMockUser(authorities = "ROLE_USER")
    @Test
    void userCannotDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/api/books/{id}", 1L))
                .andExpect(status().isForbidden());

        verify(bookService, never()).deleteBook(anyLong());
    }

    @WithAnonymousUser
    @Test
    void anonymousUserCannotDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/api/books/{id}", 1L))
                .andExpect(status().isForbidden());

        verify(bookService, never()).deleteBook(anyLong());
    }

    @Test
    void canSearchBooks() throws Exception {
        when(bookService.searchBooks(any(SearchDTO.class)))
                .thenReturn(List.of(new BookOverviewDTO(1L, "Pod igoto", testAuthor, "novel", "some picture")));

        mockMvc.perform(get("/api/books/search")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new SearchDTO("some text"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].title").value("Pod igoto"))
                .andExpect(jsonPath("$.[0].author.fullName").value("Ivan Vazov"))
                .andExpect(jsonPath("$.[0].genre").value("novel"))
                .andExpect(jsonPath("$.[0].picture").value("some picture"));

        verify(bookService, times(1)).searchBooks(any(SearchDTO.class));
    }

    @Test
    void cannotSearchBooksWithBlankSearchText() throws Exception {
        mockMvc.perform(get("/api/books/search")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new SearchDTO(""))))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).searchBooks(any(SearchDTO.class));
    }

    @Test
    void cannotSearchBooksWithoutSearchText() throws Exception {
        mockMvc.perform(get("/api/books/search")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new SearchDTO())))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).searchBooks(any(SearchDTO.class));
    }

    @Test
    void canGetBooksByAuthor() throws Exception {
        when(bookService.getBooksByAuthor(1L))
                .thenReturn(List.of(new BookOverviewDTO(1L, "Pod Igoto", testAuthor, "novel", "picture")));

        mockMvc.perform(get("/api/books?authorId={id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].title").value("Pod Igoto"))
                .andExpect(jsonPath("$.[0].author.fullName").value("Ivan Vazov"))
                .andExpect(jsonPath("$.[0].genre").value("novel"))
                .andExpect(jsonPath("$.[0].picture").value("picture"));

        verify(bookService, times(1)).getBooksByAuthor(1L);
    }

    @Test
    void testGetBooksByAuthor_Throws_AuthorIdIncorrect() throws Exception {
        when(bookService.getBooksByAuthor(1L))
                .thenThrow(AuthorNotFoundException.class);

        mockMvc.perform(get("/api/books?authorId={id}", 1L))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).getBooksByAuthor(1L);
    }
}
