package bg.softuni.onlinebookstorebackend.model.dto.book;

import bg.softuni.onlinebookstorebackend.model.validation.ExistingBookId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddBookToCartDTO {
    @ExistingBookId
    private Long bookId;
    private int quantity;
}
