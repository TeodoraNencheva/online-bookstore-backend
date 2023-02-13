package bg.softuni.onlinebookstorebackend.model.dto.book;

import bg.softuni.onlinebookstorebackend.model.entity.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookAddedToCartDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private Long authorId;
    private String authorFullName;
    private int quantity;

    public BookAddedToCartDTO(BookEntity bookEntity, int quantity) {
        this.id = bookEntity.getId();
        this.title = bookEntity.getTitle();
        this.price = bookEntity.getPrice();
        this.authorId = bookEntity.getAuthor().getId();
        this.authorFullName = bookEntity.getAuthor().getFullName();
        this.quantity = quantity;
    }
}
