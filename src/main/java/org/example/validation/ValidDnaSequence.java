package org.example.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidDnaSequenceValidator.class)
public @interface ValidDnaSequence {
    String message() default "Invalid DNA sequence: must be a square NxN matrix (minimum 4x4) with only A, T, C, G characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
