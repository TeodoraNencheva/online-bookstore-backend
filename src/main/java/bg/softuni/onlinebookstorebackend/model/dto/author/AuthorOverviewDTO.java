package bg.softuni.onlinebookstorebackend.model.dto.author;

public class AuthorOverviewDTO {
    private Long id;
    private String fullName;

    private String picture;

    public AuthorOverviewDTO() {
    }

    public AuthorOverviewDTO(Long id, String fullName, String picture) {
        this.id = id;
        this.fullName = fullName;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
