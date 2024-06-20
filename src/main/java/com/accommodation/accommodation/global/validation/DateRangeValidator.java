package com.accommodation.accommodation.global.validation;

import com.accommodation.accommodation.global.validation.type.DateRangeValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class DateRangeValidator implements ConstraintValidator<DateRangeValidation, ValidDateRange> {

    @Override
    public boolean isValid(ValidDateRange validDateRange, ConstraintValidatorContext context) {
        if (validDateRange.getCheckInDate() == null || validDateRange.getCheckOutDate() == null) {
            return true;
        }
        return !validDateRange.getCheckInDate().isAfter(validDateRange.getCheckOutDate());
    }
}