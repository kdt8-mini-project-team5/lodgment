package com.accommodation.accommodation.global.validation;

import com.accommodation.accommodation.global.validation.type.EmailValidation;
import com.accommodation.accommodation.global.validation.type.PasswordValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {

    private Pattern pattern;
    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";

    @Override
    public void initialize(PasswordValidation constraintAnnotation) {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || !pattern.matcher(value).matches()) {
            return false;
        }
        return true;
    }

}
