package com.asx.assignment.ums.rest.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = IdValidatorHandler.class)
@Documented
public @interface IdValidator {
    String message() default "Invalid id provided. Must be a positive number.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
