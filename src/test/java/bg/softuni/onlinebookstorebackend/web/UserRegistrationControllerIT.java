package bg.softuni.onlinebookstorebackend.web;

import bg.softuni.onlinebookstorebackend.util.TestDataUtils;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.mail.internet.MimeMessage;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserRegistrationControllerIT {
    @RegisterExtension
    private GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("test", "topsecret"))
            .withPerMethodLifecycle(true);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    @BeforeEach
    void setUp() {
        greenMail.start();
        testDataUtils.initRoles();
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
        greenMail.stop();
    }


//    @Test
//    void testRegistrationPageShown() throws Exception {
//        mockMvc.perform(get("/users/register")).
//                andExpect(status().isOk()).
//                andExpect(view().name("auth-register"));
//    }

//    @Test
//    void testUserRegistration() throws Exception {
//
//        mockMvc.perform(post("/users/register")
//                        .param("email", "anna@example.com")
//                        .param("firstName", "Anna")
//                        .param("lastName", "Petrova")
//                        .param("password", "topsecret")
//                        .param("confirmPassword", "topsecret")
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("need-for-verification"));
//
//        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
//        Assertions.assertEquals(1, receivedMessages.length);
//
//        MimeMessage verificationEmail = receivedMessages[0];
//
//        Assertions.assertTrue(GreenMailUtil
//                .getBody(verificationEmail)
//                .contains("Anna Petrova"));
//        Assertions.assertEquals(1, verificationEmail.getAllRecipients().length);
//        Assertions.assertEquals("anna@example.com", verificationEmail
//                .getAllRecipients()[0].toString());
//    }
}
