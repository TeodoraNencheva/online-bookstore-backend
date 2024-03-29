package bg.softuni.onlinebookstorebackend.model.validation;

import bg.softuni.onlinebookstorebackend.repositories.AuthorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class ExistingAuthorIdValidator implements ConstraintValidator<ExistingAuthorId, Long> {
    private final AuthorRepository authorRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return Objects.nonNull(value) && authorRepository.findById(value).isPresent();
    }
}
