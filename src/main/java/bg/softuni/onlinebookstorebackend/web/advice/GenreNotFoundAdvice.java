package bg.softuni.onlinebookstorebackend.web.advice;

import bg.softuni.onlinebookstorebackend.model.error.GenreNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GenreNotFoundAdvice {
    @ExceptionHandler({GenreNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ModelAndView onGenreNotFound(GenreNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("object-not-found");
        modelAndView.addObject("title", "Genre not found");
        modelAndView.addObject("message", String.format("Genre %s not found", ex.getName()));

        return modelAndView;
    }
}
