package com.accommodation.accommodation.global.validation;

import com.accommodation.accommodation.global.validation.type.EmailValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailValidation, String> {

    private String value;

    @Override
    public void initialize(EmailValidation constraintAnnotation) {
        value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return false;
    }

}
