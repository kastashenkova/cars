package org.example.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.RECORD_COMPONENT, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LicensePlateValidator.class)
@Documented
public @interface ValidLicensePlate {
    String message() default "{validation.license-plate.format}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
