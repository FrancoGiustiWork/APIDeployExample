package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    private static final Pattern ALLOWED_CHARS_PATTERN = Pattern.compile("^[ATCG]+$");

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        if (dna == null || dna.length == 0) {
            return false;
        }

        final int n = dna.length;
        if (n < 4) {
            return false; // Minimum size is 4x4
        }

        for (String row : dna) {
            if (row == null || row.length() != n) {
                return false; // Not a square matrix
            }
            if (!ALLOWED_CHARS_PATTERN.matcher(row).matches()) {
                return false; // Contains invalid characters
            }
        }

        return true;
    }
}