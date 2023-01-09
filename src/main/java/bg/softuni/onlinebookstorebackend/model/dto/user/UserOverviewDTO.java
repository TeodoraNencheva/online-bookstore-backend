package bg.softuni.onlinebookstorebackend.model.dto.user;

public class UserOverviewDTO {
    private String fullName;
    private String username;

    public UserOverviewDTO() {
    }

    public UserOverviewDTO(String fullName, String username) {
        this.fullName = fullName;
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
