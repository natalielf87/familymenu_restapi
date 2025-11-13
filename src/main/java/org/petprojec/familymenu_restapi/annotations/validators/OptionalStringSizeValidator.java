package org.petprojec.familymenu_restapi.annotations.validators;

import java.util.Optional;

import org.petprojec.familymenu_restapi.annotations.OptionalStringSize;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OptionalStringSizeValidator  implements ConstraintValidator<OptionalStringSize, Optional<String>> {

    private int min;
    private int max;

    @Override
    public void initialize(OptionalStringSize constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Optional<String> optionalString, 
                           ConstraintValidatorContext context) {
        
        if (optionalString.isEmpty()) {
            return true;
        }

        String value = optionalString.get();

        int length = value.length();
        return ((length >= min) && (length <= max));
    }
}
