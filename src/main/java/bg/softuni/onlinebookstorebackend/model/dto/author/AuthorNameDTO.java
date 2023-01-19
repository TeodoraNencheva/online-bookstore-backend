package bg.softuni.onlinebookstorebackend.model.dto.author;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AuthorNameDTO {
    private Long id;
    private String fullName;
}
