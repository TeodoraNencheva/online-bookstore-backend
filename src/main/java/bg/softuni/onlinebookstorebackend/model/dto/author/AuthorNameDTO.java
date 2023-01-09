package bg.softuni.onlinebookstorebackend.model.dto.author;

public class AuthorNameDTO {
    private Long id;
    private String fullName;

    public AuthorNameDTO() {
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
}
