package bg.softuni.onlinebookstorebackend.user.forgotten_password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResetPasswordEmailDTO {
    @NotEmpty(message = "User email should be provided.")
    @Email(message = "User email should be valid.")
    private String email;
}
