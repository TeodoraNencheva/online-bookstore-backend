package bg.softuni.onlinebookstorebackend.web.advice;

import bg.softuni.onlinebookstorebackend.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class UsernameNotFoundAdvice {
    @ExceptionHandler({UsernameNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> onUsernameNotFound(UsernameNotFoundException ex) {
        Map<String, Object> body = ResponseService.generateGeneralResponse(String.format("User %s not found", ex.getMessage()));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
