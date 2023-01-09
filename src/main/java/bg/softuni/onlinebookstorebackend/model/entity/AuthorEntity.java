package bg.softuni.onlinebookstorebackend.model.entity;

import bg.softuni.onlinebookstorebackend.model.dto.author.AddNewAuthorDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

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

    public AuthorEntity() {
    }

    public AuthorEntity(String firstName, String lastName, String biography, PictureEntity picture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.biography = biography;
        this.picture = picture;
    }

    public AuthorEntity(AddNewAuthorDTO authorDTO, PictureEntity picture) {
        this(authorDTO);
        this.picture = picture;
    }

    public AuthorEntity(AddNewAuthorDTO authorDTO) {
        this.firstName = authorDTO.getFirstName();
        this.lastName = authorDTO.getLastName();
        this.biography = authorDTO.getBiography();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public PictureEntity getPicture() {
        return picture;
    }

    public void setPicture(PictureEntity picture) {
        this.picture = picture;
    }

    public String getFullName() {
        if (firstName == null) {
            return lastName;
        }

        return firstName + " " + lastName;
    }
}
