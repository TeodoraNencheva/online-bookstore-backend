package bg.softuni.onlinebookstorebackend.web.advice;

import bg.softuni.onlinebookstorebackend.model.error.GenreNotFoundException;
import bg.softuni.onlinebookstorebackend.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class GenreNotFoundAdvice {
    @ExceptionHandler({GenreNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> onGenreNotFound(GenreNotFoundException ex) {
        Map<String, Object> body = ResponseService.generateGeneralResponse(String.format("Genre %s not found", ex.getName()));
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
