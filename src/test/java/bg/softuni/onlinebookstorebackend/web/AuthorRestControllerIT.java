package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import bg.softuni.onlinebookstorebackend.util.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorRestControllerIT {
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

    @Test
    void testAllAuthorsPageShown() throws Exception {
           mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("authors"));
    }

    @Test
    void testAuthorDetailsPageShown() throws Exception {
        mockMvc.perform(get("/authors/{id}", testAuthor.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("author-details"))
                .andExpect(model().attributeExists("author"));
    }

    @Test
    @WithMockUser(
            username = "admin@example.com",
            authorities = "ROLE_ADMIN"
    )
    void testDeleteAuthorByAdminWorks() throws Exception {
        mockMvc.perform(delete("/authors/{id}", testAuthor.getId()).
                        with(csrf())
                ).
                andExpect(status().is3xxRedirection()).
                andExpect(view().name("redirect:/authors"));
    }

    @Test
    @WithMockUser(
            username = "user@example.com",
            authorities = "ROLE_USER"
    )
    void testDeleteAuthorByUserForbidden() throws Exception {

        mockMvc.perform(delete("/authors/{id}", testAuthor.getId()).
                        with(csrf())
                ).
                andExpect(status().isForbidden());
    }

    @Test
    void testAuthorNotFoundThrownWhenIdIncorrect() throws Exception {
        mockMvc.perform(get("/authors/100"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("object-not-found"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attributeExists("title"));
    }
}
