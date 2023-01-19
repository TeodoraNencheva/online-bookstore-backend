package bg.softuni.onlinebookstorebackend.model.entity;

import bg.softuni.onlinebookstorebackend.model.dto.author.AddNewAuthorDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "authors")
public class AuthorEntity extends BaseEntity {

    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, length = 1000)
    private String biography;

    @OneToOne
    private PictureEntity picture;

    public AuthorEntity(AddNewAuthorDTO authorDTO, PictureEntity picture) {
        this(authorDTO);
        this.picture = picture;
    }

    public AuthorEntity(AddNewAuthorDTO authorDTO) {
        this.firstName = authorDTO.getFirstName();
        this.lastName = authorDTO.getLastName();
        this.biography = authorDTO.getBiography();
    }

    public String getFullName() {
        if (firstName == null) {
            return lastName;
        }

        return firstName + " " + lastName;
    }
}
