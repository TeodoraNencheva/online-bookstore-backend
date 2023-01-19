package bg.softuni.onlinebookstorebackend.model.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookAddedToCartDTO {
    private String title;
    private String authorFullName;
    private int quantity;
}
