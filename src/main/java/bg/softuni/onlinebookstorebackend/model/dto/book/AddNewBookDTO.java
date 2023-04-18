package bg.softuni.onlinebookstorebackend.model.dto.book;

import bg.softuni.onlinebookstorebackend.model.validation.ExistingAuthorId;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddNewBookDTO {
    @NotEmpty
    private String title;

    @ExistingAuthorId
    private Long authorId;

    @NotNull
    private Long genreId;

    @NotEmpty
    private String yearOfPublication;

    @NotEmpty
    private String summary;

    @NotNull
    @Positive
    private BigDecimal price;
}
