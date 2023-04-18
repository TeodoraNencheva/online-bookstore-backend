package bg.softuni.onlinebookstorebackend.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Author not found")
public class AuthorNotFoundException extends BaseException {
    private final String message = String.format("Author with ID %s not found", this.getId());
    public AuthorNotFoundException(Long id) {
        super(id);
        super.setMessage(message);
    }
}
