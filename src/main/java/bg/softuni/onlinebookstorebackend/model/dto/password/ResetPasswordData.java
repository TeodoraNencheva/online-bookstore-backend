package bg.softuni.onlinebookstorebackend.model.dto.password;

import bg.softuni.onlinebookstorebackend.model.validation.FieldMatch;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@FieldMatch(
        first = "password",
        second = "confirmPassword",
        message = "Passwords do not match."
)
@Getter
@Setter
public class ResetPasswordData {
    private String token;

    @NotEmpty(message = "Password is required")
    @Size(min = 5, message = "Password should be at least 5 characters.")
    private String password;
    private String confirmPassword;

}
