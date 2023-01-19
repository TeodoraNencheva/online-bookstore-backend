package bg.softuni.onlinebookstorebackend.web.advice;

import bg.softuni.onlinebookstorebackend.model.dto.response.GeneralResponse;
import bg.softuni.onlinebookstorebackend.model.error.GenreNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GenreNotFoundAdvice {
    @ExceptionHandler({GenreNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<GeneralResponse> onGenreNotFound(GenreNotFoundException ex) {
        GeneralResponse body = new GeneralResponse(
                String.format("Genre %s not found", ex.getName()));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
