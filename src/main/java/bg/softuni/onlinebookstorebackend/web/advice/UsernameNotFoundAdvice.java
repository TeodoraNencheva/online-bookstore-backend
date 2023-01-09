package bg.softuni.onlinebookstorebackend.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class UsernameNotFoundAdvice {
    @ExceptionHandler({UsernameNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ModelAndView onUsernameNotFound(UsernameNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("object-not-found");
        modelAndView.addObject("title", "User not found");
        modelAndView.addObject("message", String.format("User with username %s not found", ex.getMessage()));

        return modelAndView;
    }
}
