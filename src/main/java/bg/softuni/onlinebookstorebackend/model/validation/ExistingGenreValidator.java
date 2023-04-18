package bg.softuni.onlinebookstorebackend.model.validation;

import bg.softuni.onlinebookstorebackend.repositories.GenreRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class ExistingGenreValidator implements ConstraintValidator<ExistingGenre, String> {
    private final GenreRepository genreRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Objects.nonNull(value) && genreRepository.findByNameIgnoreCase(value).isPresent();
    }
}
