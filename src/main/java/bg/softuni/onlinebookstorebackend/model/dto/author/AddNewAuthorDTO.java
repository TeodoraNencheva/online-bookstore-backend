package bg.softuni.onlinebookstorebackend.model.dto.author;

import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddNewAuthorDTO {
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 30)
    private String lastName;

    @NotEmpty
    private String biography;

    public AddNewAuthorDTO(AuthorEntity author) {
        this.firstName = author.getFirstName();
        this.lastName = author.getLastName();
        this.biography = author.getBiography();
    }
}
