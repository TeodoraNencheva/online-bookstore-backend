package bg.softuni.onlinebookstorebackend.model.validation;

import bg.softuni.onlinebookstorebackend.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

  private UserRepository userRepository;

  public UniqueUserEmailValidator(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    return userRepository.
        findByEmail(value).
        isEmpty();
  }
}
