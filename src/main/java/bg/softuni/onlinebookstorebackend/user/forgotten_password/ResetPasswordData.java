package bg.softuni.onlinebookstorebackend.user.forgotten_password;

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

    @NotEmpty
    @Size(min = 5)
    private String password;
    private String confirmPassword;

}
