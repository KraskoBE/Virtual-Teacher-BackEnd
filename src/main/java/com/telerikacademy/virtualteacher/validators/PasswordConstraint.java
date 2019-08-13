package com.telerikacademy.virtualteacher.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {
    String message() default "Minimum eight characters, at least one letter and one number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}