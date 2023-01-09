package bg.softuni.onlinebookstorebackend.web;


import bg.softuni.onlinebookstorebackend.model.dto.book.AddBookToCartDTO;
import bg.softuni.onlinebookstorebackend.model.dto.book.BookAddedToCartDTO;
import bg.softuni.onlinebookstorebackend.service.BookService;
import bg.softuni.onlinebookstorebackend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CartRestControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BookService bookService;

    @WithMockUser(authorities = "ROLE_USER", username = "user@example.com")
    @Test
    void testAddToCartWithRegularUserSuccessful() throws Exception {
        when(userService.addBookToCart(any(UserDetails.class), any(AddBookToCartDTO.class)))
                .thenReturn(true);
        when(bookService.getAddedBook(any(AddBookToCartDTO.class)))
                .thenReturn(new BookAddedToCartDTO("title", "Ivan Vazov", 1));

        ObjectMapper objectMapper = new ObjectMapper();
        AddBookToCartDTO addBookDto = new AddBookToCartDTO(1L, 1);

        mockMvc.perform(post("/api/cart")
                        .content(objectMapper.writeValueAsString(addBookDto))
                        .with(csrf())
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.authorFullName").value("Ivan Vazov"))
                .andExpect(jsonPath("$.quantity").value(1));
    }
}
