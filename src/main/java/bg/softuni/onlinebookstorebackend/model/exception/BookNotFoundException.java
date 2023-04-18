package bg.softuni.onlinebookstorebackend.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Book not found")
public class BookNotFoundException extends BaseException {
    private final String message = String.format("Book with ID %s not found", this.getId());

    public BookNotFoundException(Long id) {
        super(id);
        super.setMessage(message);
    }
}
