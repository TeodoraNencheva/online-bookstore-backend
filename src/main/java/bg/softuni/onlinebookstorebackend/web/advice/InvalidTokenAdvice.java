package bg.softuni.onlinebookstorebackend.web.advice;

import bg.softuni.onlinebookstorebackend.model.error.InvalidTokenException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class InvalidTokenAdvice {
    private final MessageSource messageSource;

    public InvalidTokenAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<Object> onInvalidToken(InvalidTokenException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", messageSource.getMessage("user.registration.verification.invalid.token",
                null,
                LocaleContextHolder.getLocale()));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
