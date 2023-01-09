package bg.softuni.onlinebookstorebackend.model.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Author not found")
public class AuthorNotFoundException extends RuntimeException {
    private Long id;

    public AuthorNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
