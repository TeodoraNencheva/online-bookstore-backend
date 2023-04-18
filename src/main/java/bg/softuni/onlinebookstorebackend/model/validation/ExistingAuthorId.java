package bg.softuni.onlinebookstorebackend.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ExistingAuthorIdValidator.class)
public @interface ExistingAuthorId {
    String message() default "Invalid author ID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
