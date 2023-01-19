package bg.softuni.onlinebookstorebackend.model.dto.book;

import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class BookDetailsDTO {
    private Long id;

    private String title;

    private AuthorEntity author;

    private String genre;

    private String yearOfPublication;

    private String summary;

    private String picture;

    private BigDecimal price;
}
