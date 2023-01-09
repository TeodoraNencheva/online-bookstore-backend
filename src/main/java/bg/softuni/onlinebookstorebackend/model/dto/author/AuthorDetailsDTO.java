package bg.softuni.onlinebookstorebackend.model.dto.author;

public class AuthorDetailsDTO {
    private Long id;
    private String fullName;
    private String biography;

    private String picture;

    public AuthorDetailsDTO() {
    }

    public AuthorDetailsDTO(Long id, String fullName, String biography, String picture) {
        this.id = id;
        this.fullName = fullName;
        this.biography = biography;
        this.picture = picture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
