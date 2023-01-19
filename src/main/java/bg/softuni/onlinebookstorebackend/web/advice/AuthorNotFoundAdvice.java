package bg.softuni.onlinebookstorebackend.web.advice;

import bg.softuni.onlinebookstorebackend.model.dto.response.GeneralResponse;
import bg.softuni.onlinebookstorebackend.model.error.AuthorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AuthorNotFoundAdvice {
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({AuthorNotFoundException.class})
    public ResponseEntity<GeneralResponse> onAuthorNotFound(AuthorNotFoundException ex) {
        GeneralResponse body = new GeneralResponse(
                String.format("Author with ID %s not found", ex.getId()));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
