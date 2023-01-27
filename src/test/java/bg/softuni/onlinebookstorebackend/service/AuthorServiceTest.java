package bg.softuni.onlinebookstorebackend.service;

import bg.softuni.onlinebookstorebackend.model.cloudinary.CloudinaryImage;
import bg.softuni.onlinebookstorebackend.model.dto.author.AddNewAuthorDTO;
import bg.softuni.onlinebookstorebackend.model.dto.search.SearchDTO;
import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import bg.softuni.onlinebookstorebackend.model.entity.PictureEntity;
import bg.softuni.onlinebookstorebackend.model.mapper.AuthorMapper;
import bg.softuni.onlinebookstorebackend.repositories.AuthorRepository;
import bg.softuni.onlinebookstorebackend.repositories.AuthorSpecification;
import bg.softuni.onlinebookstorebackend.repositories.BookRepository;
import bg.softuni.onlinebookstorebackend.repositories.PictureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private Page<AuthorEntity> page;

    private MockMultipartFile picture;
    private AddNewAuthorDTO authorModel;

    private AuthorService underTest;

    @BeforeEach
    void setUp() {
        underTest = new AuthorService(authorRepository, bookRepository, authorMapper,
                pictureRepository, cloudinaryService);
    }

    @Test
    void canGetAllAuthorsPaged() {
        when(authorRepository.findAll(any(Pageable.class))).thenReturn(page);

        Pageable pageable = PageRequest.of(0, 5, Sort.by("lastName").ascending());
        underTest.getAllAuthors(pageable);
        verify(authorRepository).findAll(pageable);
    }

    @Test
    void canGetAuthorById() {
        underTest.getAuthorById(1L);
        verify(authorRepository).findById(1L);
    }

    @Test
    void canGetAuthorDetails() {
        underTest.getAuthorDetails(1L);
        verify(authorRepository).findById(1L);
    }

    @Test
    void canAddAuthorWithPicture() throws IOException {
        picture = new MockMultipartFile("picture.png", "picture.png", "multipart/form-data", new byte[]{});
        authorModel = new AddNewAuthorDTO("Ivan", "Vazov", "biography", picture);

        CloudinaryImage cloudinaryImage = new CloudinaryImage("url", "publicId");
        AuthorEntity expected = new AuthorEntity(authorModel, new PictureEntity(cloudinaryImage));

        when(cloudinaryService.upload(any(MultipartFile.class))).thenReturn(cloudinaryImage);
        underTest.addNewAuthor(authorModel);

        ArgumentCaptor<AuthorEntity> argumentCaptor =
                ArgumentCaptor.forClass(AuthorEntity.class);

        verify(pictureRepository).save(any(PictureEntity.class));
        verify(authorRepository).save(argumentCaptor.capture());

        AuthorEntity captured = argumentCaptor.getValue();
        assertThat(captured).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void canAddAuthorWithoutPicture() throws IOException {
        authorModel = new AddNewAuthorDTO("Ivan", "Vazov", "biography", null);
        AuthorEntity expected = new AuthorEntity(authorModel, null);

        underTest.addNewAuthor(authorModel);
        ArgumentCaptor<AuthorEntity> argumentCaptor =
                ArgumentCaptor.forClass(AuthorEntity.class);

        verify(cloudinaryService, never()).upload(any(MultipartFile.class));
        verify(pictureRepository, never()).save(any(PictureEntity.class));
        verify(authorRepository).save(argumentCaptor.capture());

        AuthorEntity captured = argumentCaptor.getValue();
        assertThat(captured).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void canUpdateAuthorWithPicture() throws IOException {
        picture = new MockMultipartFile("picture.png", "picture.png", "multipart/form-data", new byte[]{});
        authorModel = new AddNewAuthorDTO("Ivan", "Vazov", "biography", picture);

        CloudinaryImage cloudinaryImage = new CloudinaryImage("url", "publicId");
        AuthorEntity expected = new AuthorEntity(authorModel, new PictureEntity(cloudinaryImage));

        when(cloudinaryService.upload(any(MultipartFile.class))).thenReturn(cloudinaryImage);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(new AuthorEntity()));
        underTest.updateAuthor(authorModel, 1L);

        ArgumentCaptor<AuthorEntity> argumentCaptor =
                ArgumentCaptor.forClass(AuthorEntity.class);

        verify(authorRepository).findById(1L);
        verify(pictureRepository).save(any(PictureEntity.class));
        verify(authorRepository).save(argumentCaptor.capture());

        AuthorEntity captured = argumentCaptor.getValue();
        assertThat(captured).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void canUpdateAuthorWithoutPicture() throws IOException {
        authorModel = new AddNewAuthorDTO("Ivan", "Vazov", "biography", picture);
        AuthorEntity expected = new AuthorEntity(authorModel, null);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(new AuthorEntity()));
        underTest.updateAuthor(authorModel, 1L);

        ArgumentCaptor<AuthorEntity> argumentCaptor =
                ArgumentCaptor.forClass(AuthorEntity.class);


        verify(authorRepository).findById(1L);
        verify(cloudinaryService, never()).upload(any(MultipartFile.class));
        verify(pictureRepository, never()).save(any(PictureEntity.class));
        verify(authorRepository).save(argumentCaptor.capture());

        AuthorEntity captured = argumentCaptor.getValue();
        assertThat(captured).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void cannotUpdateAuthorWhenAuthorDoesNotExist() throws IOException {
        picture = new MockMultipartFile("picture.png", "picture.png", "multipart/form-data", new byte[]{});
        authorModel = new AddNewAuthorDTO("Ivan", "Vazov", "biography", picture);

        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        underTest.updateAuthor(authorModel, 1L);

        verify(authorRepository).findById(1L);
        verify(cloudinaryService, never()).upload(any(MultipartFile.class));
        verify(pictureRepository, never()).save(any(PictureEntity.class));
        verify(authorRepository, never()).save(any(AuthorEntity.class));
    }

    @Test
    void canDeleteAuthor() {
        underTest.deleteAuthor(1L);
        verify(bookRepository).deleteAllByAuthor_Id(1L);
        verify(authorRepository).deleteById(1L);
    }

    @Test
    void canSearchAuthors() {
        SearchDTO searchDTO = new SearchDTO("john");
        underTest.searchAuthors(searchDTO);
        verify(authorRepository).findAll(any(AuthorSpecification.class));
    }
}
