package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.entity.BookEntity;
import bg.softuni.onlinebookstorebackend.util.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private BookEntity testBook;

    @BeforeEach
    void setUp() {
        testBook = testDataUtils.createTestBook(testDataUtils.createTestAuthor(),
                testDataUtils.createTestGenre("novel"));
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void testAllBooksPageShown() throws Exception {
        mockMvc.perform(get("/books/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"));
    }

    @Test
    void testBookDetailsPageShown() throws Exception {
        mockMvc.perform(get("/books/{id}/details", testBook.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("book-details"))
                .andExpect(model().attributeExists("book"));
    }

    @WithMockUser(authorities = "ROLE_USER")
    @Test
    void testAddBookPageRegularUserFails() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andExpect(status().isForbidden());
    }

    @WithUserDetails(value = "admin@example.com", userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testAddBookPageAdminShown() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-add-or-update"))
                .andExpect(model().attributeExists("bookModel"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @WithMockUser(authorities = "ROLE_USER")
    @Test
    void testAddBookRegularUserFails() throws Exception {
        mockMvc.perform(post("/books/add")
                        .param("title", "Pod Igoto")
                        .param("authorId", "1L")
                        .param("genreId", "1L")
                        .param("yearOfPublication", "1894")
                        .param("summary", "some summary")
                        .param("price", "20"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    void testAddBookAdminWorks() throws Exception {
        mockMvc.perform(post("/books/add")
                        .param("title", "Pod Igoto")
                        .param("authorId", testBook.getAuthor().getId().toString())
                        .param("genreId", testBook.getGenre().getId().toString())
                        .param("yearOfPublication", "1894")
                        .param("summary", "some summary")
                        .param("price", "20")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(String.format("redirect:/books/%d/details", testDataUtils.getAddedBookId())));
    }

    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    void testAddBookWithWrongParamsRedirects() throws Exception {
        mockMvc.perform(post("/books/add")
                        .param("authorId", testBook.getAuthor().getId().toString())
                        .param("genreId", testBook.getGenre().getId().toString())
                        .param("yearOfPublication", "1894")
                        .param("summary", "some summary")
                        .param("picture", "image URL")
                        .param("price", "20")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books/add"));
    }

    @WithUserDetails(value = "admin@example.com", userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testUpdateBookPageAdminShown() throws Exception {
        mockMvc.perform(get("/books/update/{id}", testBook.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("book-add-or-update"))
                .andExpect(model().attributeExists("bookModel"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @WithMockUser(authorities = "ROLE_USER")
    @Test
    void testUpdateBookRegularUserFails() throws Exception {
        mockMvc.perform(post("/books/update/{id}", testBook.getId())
                        .param("title", "Pod Igoto")
                        .param("authorId", "1L")
                        .param("genreId", "1L")
                        .param("yearOfPublication", "1894")
                        .param("summary", "some summary")
                        .param("picture", "image URL")
                        .param("price", "20"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    void testUpdateBookAdminWorks() throws Exception {
        mockMvc.perform(put("/books/{id}", testBook.getId())
                        .param("title", "Pod Igoto")
                        .param("authorId", testBook.getAuthor().getId().toString())
                        .param("genreId", testBook.getGenre().getId().toString())
                        .param("yearOfPublication", "1894")
                        .param("summary", "some summary")
                        .param("price", "20")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(String.format("redirect:/books/%d/details", testBook.getId())));
    }
}
