package bg.softuni.onlinebookstorebackend.model.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Book not found")
public class BookNotFoundException extends RuntimeException {
    private Long id;

    public BookNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
