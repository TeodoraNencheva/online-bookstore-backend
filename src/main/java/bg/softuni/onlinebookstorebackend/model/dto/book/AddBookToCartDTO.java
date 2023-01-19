package bg.softuni.onlinebookstorebackend.model.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddBookToCartDTO {
    private Long bookId;
    private int quantity;
}
