package com.accommodation.accommodation.global.validation;

import com.accommodation.accommodation.domain.auth.exception.AuthException;
import com.accommodation.accommodation.domain.auth.exception.errorcode.AuthErrorCode;
import com.accommodation.accommodation.global.validation.type.EmailValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<EmailValidation, String> {

    private Pattern pattern;
    private static final String EMAIL_PATTERN =
            "\\b[A-Za-z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b";

    @Override
    public void initialize(EmailValidation constraintAnnotation) {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || !pattern.matcher(value).matches()) {
            return false;
        }
        return true;
    }

}
