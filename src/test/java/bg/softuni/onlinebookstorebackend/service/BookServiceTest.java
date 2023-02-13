package bg.softuni.onlinebookstorebackend.service;

import bg.softuni.onlinebookstorebackend.model.cloudinary.CloudinaryImage;
import bg.softuni.onlinebookstorebackend.model.dto.book.*;
import bg.softuni.onlinebookstorebackend.model.dto.search.SearchDTO;
import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import bg.softuni.onlinebookstorebackend.model.entity.BookEntity;
import bg.softuni.onlinebookstorebackend.model.entity.GenreEntity;
import bg.softuni.onlinebookstorebackend.model.entity.PictureEntity;
import bg.softuni.onlinebookstorebackend.model.error.AuthorNotFoundException;
import bg.softuni.onlinebookstorebackend.model.error.BookNotFoundException;
import bg.softuni.onlinebookstorebackend.model.error.GenreNotFoundException;
import bg.softuni.onlinebookstorebackend.model.mapper.BookMapper;
import bg.softuni.onlinebookstorebackend.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private CloudinaryService cloudinaryService;
    @Mock
    private PictureRepository pictureRepository;

    private BookService underTest;

    @BeforeEach
    void setUp() {
        underTest = new BookService(bookRepository, authorRepository, genreRepository,
                bookMapper, cloudinaryService, pictureRepository);
    }

    @Test
    void canGetAllBooksPaged() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("title").ascending());

        when(bookRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(new BookEntity()), pageable, 1L));
        when(bookMapper.bookEntityToBookOverviewDTO(any(BookEntity.class)))
                .thenReturn(new BookOverviewDTO());

        underTest.getAllBooks(pageable);
        verify(bookRepository).findAll(pageable);
        verify(bookMapper).bookEntityToBookOverviewDTO(any(BookEntity.class));
    }

    @Test
    void canGetAllBooksByGenre() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("title").ascending());

        when(genreRepository.findByNameIgnoreCase(anyString()))
                .thenReturn(Optional.of(new GenreEntity("genre")));
        when(bookRepository.getAllByGenre(any(GenreEntity.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(new BookEntity()), pageable, 1L));
        when(bookMapper.bookEntityToBookOverviewDTO(any(BookEntity.class)))
                .thenReturn(new BookOverviewDTO());

        underTest.getBooksByGenre("novel", pageable);

        verify(genreRepository).findByNameIgnoreCase("novel");
        verify(bookRepository).getAllByGenre(any(GenreEntity.class), eq(pageable));
        verify(bookMapper).bookEntityToBookOverviewDTO(any(BookEntity.class));
    }

    @Test
    void throwsWhenGenreDoesNotExist() {
        when(genreRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        Pageable pageable = PageRequest.of(0, 5, Sort.by("title").ascending());

        assertThatThrownBy(() -> underTest.getBooksByGenre("genre", pageable))
                .isInstanceOf(GenreNotFoundException.class);

        verify(genreRepository).findByNameIgnoreCase("genre");
        verify(bookRepository, never()).getAllByGenre(any(GenreEntity.class), any(Pageable.class));
        verify(bookMapper, never()).bookEntityToBookOverviewDTO(any(BookEntity.class));
    }

    @Test
    void canGetBookDetails() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(new BookEntity()));
        when(bookMapper.bookEntityToBookDetailsDTO(any(BookEntity.class)))
                .thenReturn(new BookDetailsDTO());

        underTest.getBookDetails(1L);

        verify(bookRepository).findById(1L);
        verify(bookMapper).bookEntityToBookDetailsDTO(any(BookEntity.class));
    }

    @Test
    void throwsWhenBookDoesNotExist() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getBookDetails(1L))
                .isInstanceOf(BookNotFoundException.class);

        verify(bookRepository).findById(1L);
        verify(bookMapper, never()).bookEntityToBookDetailsDTO(any(BookEntity.class));
    }

    @Test
    void canAddNewBookWithPicture() throws IOException {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(new AuthorEntity()));
        when(genreRepository.findById(anyLong())).thenReturn(Optional.of(new GenreEntity()));

        MockMultipartFile picture = new MockMultipartFile("picture.png", "picture.png", "multipart/form-data", new byte[]{});
        AddNewBookDTO bookModel = new AddNewBookDTO("title", 1L, 1L, "2000", "summary", picture, new BigDecimal("20"));

        CloudinaryImage cloudinaryImage = new CloudinaryImage("url", "publicId");
        BookEntity expected = new BookEntity(bookModel, new AuthorEntity(), new GenreEntity(), new PictureEntity(cloudinaryImage));

        when(cloudinaryService.upload(any(MultipartFile.class))).thenReturn(cloudinaryImage);

        underTest.addNewBook(bookModel);

        ArgumentCaptor<BookEntity> argumentCaptor =
                ArgumentCaptor.forClass(BookEntity.class);

        verify(authorRepository).findById(1L);
        verify(genreRepository).findById(1L);
        verify(cloudinaryService).upload(picture);
        verify(pictureRepository).save(any(PictureEntity.class));
        verify(bookRepository).save(argumentCaptor.capture());

        BookEntity captured = argumentCaptor.getValue();
        assertThat(captured).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void canAddNewBookWithoutPicture() throws IOException {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(new AuthorEntity()));
        when(genreRepository.findById(anyLong())).thenReturn(Optional.of(new GenreEntity()));

        AddNewBookDTO bookModel = new AddNewBookDTO("title", 1L, 1L, "2000", "summary", null, new BigDecimal("20"));

        BookEntity expected = new BookEntity(bookModel, new AuthorEntity(), new GenreEntity(), null);

        underTest.addNewBook(bookModel);

        ArgumentCaptor<BookEntity> argumentCaptor =
                ArgumentCaptor.forClass(BookEntity.class);

        verify(authorRepository).findById(1L);
        verify(genreRepository).findById(1L);
        verify(cloudinaryService, never()).upload(any(MultipartFile.class));
        verify(pictureRepository, never()).save(any(PictureEntity.class));
        verify(bookRepository).save(argumentCaptor.capture());

        BookEntity captured = argumentCaptor.getValue();
        assertThat(captured).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void addNewBookThrowsWhenAuthorDoesNotExist() throws IOException {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(genreRepository.findById(anyLong())).thenReturn(Optional.of(new GenreEntity()));

        AddNewBookDTO bookModel = new AddNewBookDTO("title", 1L, 1L, "2000", "summary", null, new BigDecimal("20"));

        assertThatThrownBy(() -> underTest.addNewBook(bookModel))
                .isInstanceOf(AuthorNotFoundException.class);

        verify(authorRepository).findById(1L);
        verify(genreRepository).findById(1L);
        verify(cloudinaryService, never()).upload(any(MultipartFile.class));
        verify(pictureRepository, never()).save(any(PictureEntity.class));
        verify(bookRepository, never()).save(any(BookEntity.class));
    }

    @Test
    void addNewBookThrowsWhenGenreDoesNotExist() throws IOException {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(new AuthorEntity()));
        when(genreRepository.findById(anyLong())).thenReturn(Optional.empty());

        AddNewBookDTO bookModel = new AddNewBookDTO("title", 1L, 1L, "2000", "summary", null, new BigDecimal("20"));

        assertThatThrownBy(() -> underTest.addNewBook(bookModel))
                .isInstanceOf(GenreNotFoundException.class);

        verify(authorRepository).findById(1L);
        verify(genreRepository).findById(1L);
        verify(cloudinaryService, never()).upload(any(MultipartFile.class));
        verify(pictureRepository, never()).save(any(PictureEntity.class));
        verify(bookRepository, never()).save(any(BookEntity.class));
    }

    @Test
    void canUpdateBookWithPicture() throws IOException {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(new BookEntity()));
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(new AuthorEntity()));
        when(genreRepository.findById(anyLong())).thenReturn(Optional.of(new GenreEntity()));

        MockMultipartFile picture = new MockMultipartFile("picture.png", "picture.png", "multipart/form-data", new byte[]{});
        AddNewBookDTO bookModel = new AddNewBookDTO("title", 1L, 1L, "2000", "summary", picture, new BigDecimal("20"));

        CloudinaryImage cloudinaryImage = new CloudinaryImage("url", "publicId");
        BookEntity expected = new BookEntity(bookModel, new AuthorEntity(), new GenreEntity(), new PictureEntity(cloudinaryImage));

        when(cloudinaryService.upload(any(MultipartFile.class))).thenReturn(cloudinaryImage);

        underTest.updateBook(bookModel, 1L);

        ArgumentCaptor<BookEntity> argumentCaptor =
                ArgumentCaptor.forClass(BookEntity.class);

        verify(bookRepository).findById(1L);
        verify(authorRepository).findById(1L);
        verify(genreRepository).findById(1L);
        verify(cloudinaryService).upload(picture);
        verify(pictureRepository).save(any(PictureEntity.class));
        verify(bookRepository).save(argumentCaptor.capture());

        BookEntity captured = argumentCaptor.getValue();
        assertThat(captured).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void canUpdateBookWithoutPicture() throws IOException {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(new BookEntity()));
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(new AuthorEntity()));
        when(genreRepository.findById(anyLong())).thenReturn(Optional.of(new GenreEntity()));

        AddNewBookDTO bookModel = new AddNewBookDTO("title", 1L, 1L, "2000", "summary", null, new BigDecimal("20"));

        BookEntity expected = new BookEntity(bookModel, new AuthorEntity(), new GenreEntity(), null);

        underTest.updateBook(bookModel, 1L);

        ArgumentCaptor<BookEntity> argumentCaptor =
                ArgumentCaptor.forClass(BookEntity.class);

        verify(bookRepository).findById(1L);
        verify(authorRepository).findById(1L);
        verify(genreRepository).findById(1L);
        verify(cloudinaryService, never()).upload(any(MultipartFile.class));
        verify(pictureRepository, never()).save(any(PictureEntity.class));
        verify(bookRepository).save(argumentCaptor.capture());

        BookEntity captured = argumentCaptor.getValue();
        assertThat(captured).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void updateBookThrowsWhenBookDoesNotExist() throws IOException {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        AddNewBookDTO bookModel = new AddNewBookDTO("title", 1L, 1L, "2000", "summary", null, new BigDecimal("20"));

        assertThatThrownBy(() -> underTest.updateBook(bookModel, 1L))
                .isInstanceOf(BookNotFoundException.class);

        verify(bookRepository).findById(1L);
        verify(authorRepository, never()).findById(1L);
        verify(genreRepository, never()).findById(1L);
        verify(cloudinaryService, never()).upload(any(MultipartFile.class));
        verify(pictureRepository, never()).save(any(PictureEntity.class));
        verify(bookRepository, never()).save(any(BookEntity.class));
    }

    @Test
    void updateBookThrowsWhenAuthorDoesNotExist() throws IOException {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(new BookEntity()));
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(genreRepository.findById(anyLong())).thenReturn(Optional.of(new GenreEntity()));

        AddNewBookDTO bookModel = new AddNewBookDTO("title", 1L, 1L, "2000", "summary", null, new BigDecimal("20"));

        assertThatThrownBy(() -> underTest.updateBook(bookModel, 1L))
                .isInstanceOf(AuthorNotFoundException.class);

        verify(bookRepository).findById(1L);
        verify(authorRepository).findById(1L);
        verify(genreRepository).findById(1L);
        verify(cloudinaryService, never()).upload(any(MultipartFile.class));
        verify(pictureRepository, never()).save(any(PictureEntity.class));
        verify(bookRepository, never()).save(any(BookEntity.class));
    }

    @Test
    void updateBookThrowsWhenGenreDoesNotExist() throws IOException {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(new BookEntity()));
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(new AuthorEntity()));
        when(genreRepository.findById(anyLong())).thenReturn(Optional.empty());

        AddNewBookDTO bookModel = new AddNewBookDTO("title", 1L, 1L, "2000", "summary", null, new BigDecimal("20"));

        assertThatThrownBy(() -> underTest.updateBook(bookModel, 1L))
                .isInstanceOf(GenreNotFoundException.class);

        verify(bookRepository).findById(1L);
        verify(authorRepository).findById(1L);
        verify(genreRepository).findById(1L);
        verify(cloudinaryService, never()).upload(any(MultipartFile.class));
        verify(pictureRepository, never()).save(any(PictureEntity.class));
        verify(bookRepository, never()).save(any(BookEntity.class));
    }

    @Test
    void canDeleteBook() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(new BookEntity()));
        underTest.deleteBook(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteThrowsWhenBookDoesNotExist() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.deleteBook(1L))
                .isInstanceOf(BookNotFoundException.class);
        verify(bookRepository, never()).deleteById(1L);
    }

    @Test
    void canSearchAuthors() {
        when(bookRepository.findAll(any(BookSpecification.class)))
                .thenReturn(List.of(new BookEntity()));
        when(bookMapper.bookEntityToBookOverviewDTO(any(BookEntity.class)))
                .thenReturn(new BookOverviewDTO());

        underTest.searchBooks(new SearchDTO("story"));
        verify(bookRepository).findAll(any(BookSpecification.class));
        verify(bookMapper).bookEntityToBookOverviewDTO(any(BookEntity.class));
    }

    @Test
    void canGetBooksByAuthor() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(new AuthorEntity()));
        when(bookRepository.getAllByAuthor(any(AuthorEntity.class)))
                .thenReturn(List.of(new BookEntity()));
        when(bookMapper.bookEntityToBookOverviewDTO(any(BookEntity.class)))
                .thenReturn(new BookOverviewDTO());

        underTest.getBooksByAuthor(1L);

        verify(authorRepository).findById(1L);
        verify(bookRepository).getAllByAuthor(any(AuthorEntity.class));
        verify(bookMapper).bookEntityToBookOverviewDTO(any(BookEntity.class));
    }

    @Test
    void getBooksByAuthorThrowsWhenAuthorDoesNotExist() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getBooksByAuthor(1L))
                .isInstanceOf(AuthorNotFoundException.class);

        verify(authorRepository).findById(1L);
        verify(bookRepository, never()).getAllByAuthor(any(AuthorEntity.class));
        verify(bookMapper, never()).bookEntityToBookOverviewDTO(any(BookEntity.class));
    }

    @Test
    void canGetAddedBook() {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setTitle("title");
        bookEntity.setAuthor(new AuthorEntity());
        bookEntity.getAuthor().setFirstName("John");
        bookEntity.getAuthor().setLastName("Doe");

        BookAddedToCartDTO expected = new BookAddedToCartDTO(bookEntity, 1);

        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.of(bookEntity));

        BookAddedToCartDTO result = underTest.getAddedBook(new AddBookToCartDTO(1L, 1));

        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void getAddedBookThrowsWhenBookDoesNotExist() {
        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getAddedBook(new AddBookToCartDTO(1L, 1)))
                .isInstanceOf(BookNotFoundException.class);
    }
}