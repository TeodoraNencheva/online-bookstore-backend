package bg.softuni.onlinebookstorebackend.model.dto.user;

import bg.softuni.onlinebookstorebackend.model.validation.FieldMatch;
import bg.softuni.onlinebookstorebackend.model.validation.UniqueUserEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FieldMatch(
        first = "password",
        second = "confirmPassword",
        message = "Passwords do not match."
)
public class UserRegistrationDTO {
    @NotEmpty(message = "User email should be provided.")
    @Email(message = "User email should be valid.")
    @UniqueUserEmail(message = "User email should be unique.")
    private String email;

    @NotEmpty
    @Size(min = 2, max = 20)
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 20)
    private String lastName;

    @NotEmpty
    @Size(min = 5, message = "Password should be 5 or more characters long.")
    private String password;

    private String confirmPassword;

    @NotEmpty(message = "Base URL should be provided.")
    private String baseUrl;
}
