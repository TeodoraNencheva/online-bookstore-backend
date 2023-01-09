package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.model.entity.OrderEntity;
import bg.softuni.onlinebookstorebackend.model.entity.UserEntity;
import bg.softuni.onlinebookstorebackend.util.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class OrderControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private OrderEntity testOrder;
    private UserEntity secondUser;

    @BeforeEach
    void setUp() {
        testOrder = testDataUtils.createTestOrder();
        secondUser = testDataUtils.createTestUser("user2@example.com");
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testGetOrdersRegularUser_Forbidden() throws Exception {
        mockMvc.perform(get("/orders/processed"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/orders/unprocessed"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "admin@example.com", userDetailsServiceBeanName = "testUserDataService")
    void testGetOrdersAdmin_Success() throws Exception {
        mockMvc.perform(get("/orders/processed"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attribute("type", "Processed"))
                .andExpect(model().attributeExists("orders"));

        mockMvc.perform(get("/orders/unprocessed"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attribute("type", "Unprocessed"))
                .andExpect(model().attributeExists("orders"));
        ;
    }

    @WithUserDetails(value = "user@example.com", userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testGetMyOrdersLoggedUser_Success() throws Exception {
        mockMvc.perform(get("/orders/mine"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attribute("type", "My"))
                .andExpect(model().attributeExists("orders"));
    }

    @WithUserDetails(value = "user@example.com", userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testGetOrderDetails_OrderOwned_Success() throws Exception {
        mockMvc.perform(get("/orders/{id}/details", testOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("order-details"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("items"));
    }

    @WithUserDetails(value = "user2@example.com", userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testGetOrderDetails_OrderNotOwned_IsForbidden() throws Exception {
        mockMvc.perform(get("/orders/{id}/details", testOrder.getId()))
                .andExpect(status().isForbidden());
    }

    @WithUserDetails(value = "user2@example.com", userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testWrongOrderId_Throws() throws Exception {
        UUID uuid = UUID.randomUUID();
        while (uuid == testOrder.getId()) {
            uuid = UUID.randomUUID();
        }

        mockMvc.perform(get("/orders/{uuid}/details", uuid))
                .andExpect(status().isNotFound())
                .andExpect(view().name("object-not-found"))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attributeExists("message"));
    }
}
