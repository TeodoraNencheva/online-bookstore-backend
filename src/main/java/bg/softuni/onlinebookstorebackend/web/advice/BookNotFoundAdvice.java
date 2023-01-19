package bg.softuni.onlinebookstorebackend.web.advice;

import bg.softuni.onlinebookstorebackend.model.dto.response.GeneralResponse;
import bg.softuni.onlinebookstorebackend.model.error.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BookNotFoundAdvice {
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({BookNotFoundException.class})
    public ResponseEntity<GeneralResponse> onBookNotFound(BookNotFoundException ex) {
        GeneralResponse body = new GeneralResponse(
                String.format("Book with ID %s not found", ex.getId()));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
