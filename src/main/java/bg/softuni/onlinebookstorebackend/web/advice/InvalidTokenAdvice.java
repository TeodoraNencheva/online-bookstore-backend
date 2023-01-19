package bg.softuni.onlinebookstorebackend.web.advice;

import bg.softuni.onlinebookstorebackend.model.dto.response.GeneralResponse;
import bg.softuni.onlinebookstorebackend.model.error.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@RequiredArgsConstructor
public class InvalidTokenAdvice {
    private final MessageSource messageSource;

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<GeneralResponse> onInvalidToken(InvalidTokenException ex) {
        GeneralResponse body = new GeneralResponse(
                messageSource.getMessage("user.registration.verification.invalid.token",
                        null,
                        LocaleContextHolder.getLocale()));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
