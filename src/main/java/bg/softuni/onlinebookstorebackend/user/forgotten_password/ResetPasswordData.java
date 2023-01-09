package bg.softuni.onlinebookstorebackend.user.forgotten_password;

import bg.softuni.onlinebookstorebackend.model.validation.FieldMatch;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


@FieldMatch(
        first = "password",
        second = "confirmPassword",
        message = "Passwords do not match."
)
public class ResetPasswordData {
    private String token;

    @NotEmpty
    @Size(min = 5)
    private String password;
    private String confirmPassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
