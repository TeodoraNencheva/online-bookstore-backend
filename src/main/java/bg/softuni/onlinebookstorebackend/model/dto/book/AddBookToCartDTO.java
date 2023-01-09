package bg.softuni.onlinebookstorebackend.model.dto.book;

public class AddBookToCartDTO {
    private Long bookId;
    private int quantity;

    public AddBookToCartDTO() {
    }

    public AddBookToCartDTO(Long bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
