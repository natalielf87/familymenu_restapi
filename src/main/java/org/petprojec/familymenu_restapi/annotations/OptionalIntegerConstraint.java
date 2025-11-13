package org.petprojec.familymenu_restapi.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.petprojec.familymenu_restapi.annotations.validators.OptionalIntegerConstraintValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OptionalIntegerConstraintValidator.class)
@Documented
public @interface OptionalIntegerConstraint {
    // Standard validation properties
    String message() default "Value must be between {min} and {max}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // Properties specific to size constraints
    int min() default Integer.MIN_VALUE;
    int max() default Integer.MAX_VALUE;
}
