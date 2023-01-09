package bg.softuni.onlinebookstorebackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class OpeAPIConfig {
    @Configuration
    public static class OpenAPIConfig {

        @Bean
        public OpenAPI openAPI() {
            return new OpenAPI().
                    info(new Info()
                            .title("Bookstore API")
                            .version("1.0.0")
                            .contact(new Contact().name("Teodora Nencheva")
                                    .email("teodora.nencheva1@gmail.com"))
                            .description("Bookstore API"));
        }

    }
}
