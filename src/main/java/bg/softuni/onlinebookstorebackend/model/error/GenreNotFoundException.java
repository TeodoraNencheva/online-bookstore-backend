package bg.softuni.onlinebookstorebackend.model.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Genre not found")
public class GenreNotFoundException extends RuntimeException {
    private String name;

    public GenreNotFoundException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
