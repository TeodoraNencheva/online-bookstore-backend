package bg.softuni.onlinebookstorebackend.model.validation;

import bg.softuni.onlinebookstorebackend.repositories.AuthorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExistingAuthorIdValidator implements ConstraintValidator<ExistingAuthorId, Long> {
    private final AuthorRepository authorRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return authorRepository.findById(value).isPresent();
    }
}
