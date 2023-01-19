package bg.softuni.onlinebookstorebackend.model.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Author not found")
public class AuthorNotFoundException extends RuntimeException {
    private Long id;
}
