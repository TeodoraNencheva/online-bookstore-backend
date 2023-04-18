package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.author.AddNewAuthorDTO;
import bg.softuni.onlinebookstorebackend.model.dto.author.AuthorDetailsDTO;
import bg.softuni.onlinebookstorebackend.model.dto.author.AuthorOverviewDTO;
import bg.softuni.onlinebookstorebackend.model.dto.search.SearchDTO;
import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import bg.softuni.onlinebookstorebackend.repositories.AuthorRepository;
import bg.softuni.onlinebookstorebackend.service.AuthorService;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    private MockMultipartFile picture;
    private AddNewAuthorDTO authorDto;
    private ObjectMapper objectMapper;
    private MockMultipartFile author;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        picture = new MockMultipartFile("picture", "picture.png",
                "multipart/form-data", new byte[]{});
        authorDto = new AddNewAuthorDTO("Ivan", "Vazov", "biography");

        objectMapper = new ObjectMapper();
        author = new MockMultipartFile("authorModel", "",
                "application/json", objectMapper.writeValueAsString(authorDto).getBytes());
    }

    @Test
    void canGetAllAuthors() throws Exception {
        when(authorService.getAllAuthors(any(Pageable.class)))
                .thenReturn(List.of(new AuthorOverviewDTO(1L, "Ivan Vazov", "url")));

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].fullName").value("Ivan Vazov"))
                .andExpect(jsonPath("$.[0].picture").value("url"));

        verify(authorService, times(1)).getAllAuthors(any(Pageable.class));
    }

    @Test
    void canGetAuthorDetails() throws Exception {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(new AuthorEntity()));
        when(authorService.getAuthorDetails(anyLong()))
                .thenReturn(new AuthorDetailsDTO(1L, "Ivan Vazov", "biography", "picture"));

        mockMvc.perform(get("/api/authors/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullName").value("Ivan Vazov"))
                .andExpect(jsonPath("$.biography").value("biography"))
                .andExpect(jsonPath("$.picture").value("picture"));

        verify(authorService, times(1)).getAuthorDetails(1L);
    }

    @Test
    void cannotGetAuthorDetailsWhenAuthorDoesNotExist() throws Exception {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/authors/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid author ID"));
    }

    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    void adminCanAddNewAuthor() throws Exception {
        AuthorEntity toReturn = new AuthorEntity(authorDto);

        when(authorService.addNewAuthor(any(AddNewAuthorDTO.class), any(MultipartFile.class)))
                .thenReturn(toReturn);

        mockMvc.perform(multipart("/api/authors/add")
                        .file(author)
                        .file(picture))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.lastName").value("Vazov"))
                .andExpect(jsonPath("$.biography").value("biography"));

        verify(authorService, times(1)).addNewAuthor(any(AddNewAuthorDTO.class), any(MultipartFile.class));
    }

    @WithMockUser(authorities = "ROLE_USER")
    @Test
    void throwsWhenUserAddsAuthor() throws Exception {
        mockMvc.perform(multipart("/api/authors/add")
                        .file(author)
                        .file(picture))
                .andExpect(status().isForbidden());

        verify(authorService, never()).addNewAuthor(any(AddNewAuthorDTO.class));
    }

    @WithAnonymousUser
    @Test
    void throwsWhenAnonymousUserAddsAuthor() throws Exception {
        mockMvc.perform(multipart("/api/authors/add")
                        .file(author)
                        .file(picture))
                .andExpect(status().isForbidden());

        verify(authorService, never()).addNewAuthor(any(AddNewAuthorDTO.class));
    }

    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    void adminCanUpdateAuthor() throws Exception {
        AuthorEntity toReturn = new AuthorEntity(authorDto);

        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(new AuthorEntity()));
        when(authorService.updateAuthor(any(AddNewAuthorDTO.class), anyLong(), any(MultipartFile.class)))
                .thenReturn(toReturn);

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/authors/{id}", 1L)
                        .file(author)
                        .file(picture))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.lastName").value("Vazov"))
                .andExpect(jsonPath("$.biography").value("biography"));

        verify(authorService, times(1)).updateAuthor(any(AddNewAuthorDTO.class), anyLong(), any(MultipartFile.class));
    }

    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    void adminCannotUpdateAuthorWhenAuthorDoesNotExist() throws Exception {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/authors/{id}", 1L)
                        .file(author)
                        .file(picture))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid author ID"));
    }

    @WithMockUser(authorities = "ROLE_USER")
    @Test
    void throwsWhenUserUpdatesAuthor() throws Exception {
        mockMvc.perform(multipart(HttpMethod.PUT, "/api/authors/{id}", 1L)
                        .file(author)
                        .file(picture))
                .andExpect(status().isForbidden());

        verify(authorService, never()).updateAuthor(any(AddNewAuthorDTO.class), anyLong());
    }

    @WithAnonymousUser
    @Test
    void throwsWhenAnonymousUserUpdatesAuthor() throws Exception {
        mockMvc.perform(multipart(HttpMethod.PUT, "/api/authors/{id}", 1L)
                        .file(author)
                        .file(picture))
                .andExpect(status().isForbidden());

        verify(authorService, never()).updateAuthor(any(AddNewAuthorDTO.class), anyLong());
    }

    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    void adminCanDeleteAuthor() throws Exception {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(new AuthorEntity()));
        doNothing().when(authorService).deleteAuthor(anyLong());

        mockMvc.perform(delete("/api/authors/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Author with ID 1 deleted"));

        verify(authorService, times(1)).deleteAuthor(1L);
    }

    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    void adminCannotDeleteAuthorWhenAuthorDoesNotExist() throws Exception {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/authors/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid author ID"));
    }

    @WithMockUser(authorities = "ROLE_USER")
    @Test
    void throwsWhenUserDeletesAuthor() throws Exception {
        mockMvc.perform(delete("/api/authors/{id}", 1L))
                .andExpect(status().isForbidden());

        verify(authorService, never()).deleteAuthor(1L);
    }

    @WithAnonymousUser
    @Test
    void throwsWhenAnonymousUserDeletesAuthor() throws Exception {
        mockMvc.perform(delete("/api/authors/{id}", 1L))
                .andExpect(status().isForbidden());

        verify(authorService, never()).deleteAuthor(1L);
    }

    @Test
    void canSearchAuthors() throws Exception {
        when(authorService.searchAuthors(any(SearchDTO.class)))
                .thenReturn(List.of(new AuthorOverviewDTO(1L, "Ivan Vazov", "picture")));

        mockMvc.perform(get("/api/authors/search")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new SearchDTO("search text"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].fullName").value("Ivan Vazov"))
                .andExpect(jsonPath("$.[0].picture").value("picture"));

        verify(authorService, times(1)).searchAuthors(any(SearchDTO.class));
    }

    @Test
    void cannotSearchAuthorsWithBlankSearchText() throws Exception {
        mockMvc.perform(get("/api/authors/search")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new SearchDTO(""))))
                .andExpect(status().isBadRequest());

        verify(authorService, never()).searchAuthors(any(SearchDTO.class));
    }

    @Test
    void cannotSearchAuthorsWithNoSearchText() throws Exception {
        mockMvc.perform(get("/api/authors/search")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new SearchDTO())))
                .andExpect(status().isBadRequest());

        verify(authorService, never()).searchAuthors(any(SearchDTO.class));
    }
}