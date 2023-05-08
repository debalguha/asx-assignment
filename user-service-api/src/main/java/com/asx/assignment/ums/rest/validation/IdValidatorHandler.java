package com.asx.assignment.ums.rest.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class IdValidatorHandler implements ConstraintValidator<IdValidator, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null || "".equals(s)) {
            return true;
        } else {
            return Optional.of(s)
                    .filter(this:: canParseToLong)
                    .map(Long::parseLong)
                    .map(l -> l > 0)
                    .orElse(false);
        }
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
