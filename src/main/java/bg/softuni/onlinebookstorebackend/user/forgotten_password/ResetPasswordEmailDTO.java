package bg.softuni.onlinebookstorebackend.user.forgotten_password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class ResetPasswordEmailDTO {
    @NotEmpty(message = "User email should be provided.")
    @Email(message = "User email should be valid.")
    private String email;

    public ResetPasswordEmailDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
