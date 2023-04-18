package bg.softuni.onlinebookstorebackend.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Genre not found")
public class GenreNotFoundException extends RuntimeException {
    private String name;
}
