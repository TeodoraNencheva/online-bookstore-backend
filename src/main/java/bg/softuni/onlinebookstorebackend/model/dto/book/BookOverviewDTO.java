package bg.softuni.onlinebookstorebackend.model.dto.book;


import bg.softuni.onlinebookstorebackend.model.entity.AuthorEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookOverviewDTO {
    private Long id;
    private String title;
    private AuthorEntity author;
    private String genre;
    private String picture;
}
