package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class LicensePlateValidator implements ConstraintValidator<ValidLicensePlate, String> {

    private static final Pattern UKRAINIAN_LICENSE_PLATE_PATTERN
            = Pattern.compile("^[АВЕІКМНОРСТХ]{2}\\d{4}[АВЕІКМНОРСТХ]{2}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return UKRAINIAN_LICENSE_PLATE_PATTERN.matcher(value.trim()).matches();
    }
}
