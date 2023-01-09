package bg.softuni.onlinebookstorebackend.model.dto.book;

public class BookAddedToCartDTO {
    private String title;
    private String authorFullName;
    private int quantity;

    public BookAddedToCartDTO() {
    }

    public BookAddedToCartDTO(String title, String authorFullName, int quantity) {
        this.title = title;
        this.authorFullName = authorFullName;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
