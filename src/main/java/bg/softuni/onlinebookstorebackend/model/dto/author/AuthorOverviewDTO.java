package bg.softuni.onlinebookstorebackend.model.dto.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthorOverviewDTO {
    private Long id;
    private String fullName;
    private String picture;
}
