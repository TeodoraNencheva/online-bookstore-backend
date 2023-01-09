package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.dto.book.BookOverviewDTO;
import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import bg.softuni.onlinebookstorebackend.model.error.AuthorNotFoundException;
import bg.softuni.onlinebookstorebackend.service.BookService;
import bg.softuni.onlinebookstorebackend.util.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookRestControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private AuthorEntity testAuthor;

    @BeforeEach
    void setUp() {
        testAuthor = testDataUtils.createTestAuthor();
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @MockBean
    private BookService bookService;

    @Test
    void testGetBooksByAuthor() throws Exception {
        when(bookService.getBooksByAuthor(1L))
                .thenReturn(List.of(new BookOverviewDTO(1L, "Pod Igoto", testAuthor, "novel", "image1"),
                        new BookOverviewDTO(2L, "Chichovci", testAuthor, "povest", "image2")));

        mockMvc.perform(get("/api/books?authorId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].title").value("Pod Igoto"))
                .andExpect(jsonPath("$.[0].author.fullName").value("Ivan Vazov"))
                .andExpect(jsonPath("$.[0].genre").value("novel"))
                .andExpect(jsonPath("$.[0].picture").value("image1"))
                .andExpect(jsonPath("$.[1].id").value(2L))
                .andExpect(jsonPath("$.[1].title").value("Chichovci"))
                .andExpect(jsonPath("$.[1].author.fullName").value("Ivan Vazov"))
                .andExpect(jsonPath("$.[1].genre").value("povest"))
                .andExpect(jsonPath("$.[1].picture").value("image2"));
    }

    @Test
    void testGetBooksByAuthor_Throws_AuthorIdIncorrect() throws Exception {
        when(bookService.getBooksByAuthor(1L))
                .thenThrow(AuthorNotFoundException.class);


        mockMvc.perform(get("/api/books?authorId=1"))
                .andExpect(status().isNotFound());
    }
}
