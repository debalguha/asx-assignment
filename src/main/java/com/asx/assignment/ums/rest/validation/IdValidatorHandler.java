package com.asx.assignment.ums.rest.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class IdValidatorHandler implements ConstraintValidator<IdValidator, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(s)
                .filter(this:: canParseToLong)
                .map(Long::parseLong)
                .filter(l -> l > 0)
                .isPresent();
    }

    private boolean canParseToLong(String input) {
        try {
            Long.parseLong(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
