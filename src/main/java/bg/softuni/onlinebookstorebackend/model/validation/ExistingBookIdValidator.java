package bg.softuni.onlinebookstorebackend.model.validation;

import bg.softuni.onlinebookstorebackend.repositories.BookRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class ExistingBookIdValidator implements ConstraintValidator<ExistingBookId, Long> {
    private final BookRepository bookRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return Objects.nonNull(value) && bookRepository.findById(value).isPresent();
    }
}
