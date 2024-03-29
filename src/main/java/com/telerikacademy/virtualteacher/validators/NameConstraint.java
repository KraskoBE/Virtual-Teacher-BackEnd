package com.telerikacademy.virtualteacher.validators;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NameConstraint {
    String message() default "Name should contain a-z and be 3-15 symbols long";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}