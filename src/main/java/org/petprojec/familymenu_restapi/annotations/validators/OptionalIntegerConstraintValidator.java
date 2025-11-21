package org.petprojec.familymenu_restapi.annotations.validators;

import java.util.Optional;

import org.petprojec.familymenu_restapi.annotations.OptionalIntegerConstraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OptionalIntegerConstraintValidator implements ConstraintValidator<OptionalIntegerConstraint, Optional<Integer>> {
    private int min;
    private int max;

    @Override
    public void initialize(OptionalIntegerConstraint annotation) {
        this.min = annotation.min();
        this.max = annotation.max();
    }

    @Override
    public boolean isValid(Optional<Integer> value, ConstraintValidatorContext context) {
       if (value == null) {
        return true;
       }
       
        if (value.isEmpty()) {
        return true;
       }

       Integer i = value.get();

       return ((i >= min) && (i<=max));
    }
    
}
