package bg.softuni.onlinebookstorebackend.service;

import bg.softuni.onlinebookstorebackend.model.dto.author.AddNewAuthorDTO;
import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import bg.softuni.onlinebookstorebackend.model.mapper.AuthorMapper;
import bg.softuni.onlinebookstorebackend.repositories.AuthorRepository;
import bg.softuni.onlinebookstorebackend.repositories.BookRepository;
import bg.softuni.onlinebookstorebackend.repositories.PictureRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

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

    private AuthorService toTest;

    @BeforeEach
    void setUp() {
        toTest = new AuthorService(authorRepository, bookRepository, authorMapper,
                pictureRepository, cloudinaryService);
    }

    @Test
    void testGetAuthorById_AuthorExists() {
        AuthorEntity testAuthorEntity = new AuthorEntity();
        testAuthorEntity.setId(1L);
        testAuthorEntity.setFirstName("Test");
        testAuthorEntity.setLastName("Author");
        testAuthorEntity.setBiography("some author biography");

        when(authorRepository.findById(testAuthorEntity.getId()))
                .thenReturn(Optional.of(testAuthorEntity));

        AddNewAuthorDTO author = toTest.getAuthorById(testAuthorEntity.getId());

        Assertions.assertEquals(testAuthorEntity.getFirstName(), author.getFirstName());
        Assertions.assertEquals(testAuthorEntity.getLastName(), author.getLastName());
        Assertions.assertEquals(testAuthorEntity.getBiography(), author.getBiography());
    }
}
