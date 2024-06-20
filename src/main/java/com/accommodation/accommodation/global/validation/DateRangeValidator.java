package com.accommodation.accommodation.global.validation;

import com.accommodation.accommodation.domain.accommodation.model.request.AccommodationDetailRequest;
import com.accommodation.accommodation.global.validation.type.DateRangeValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<DateRangeValidation, AccommodationDetailRequest> {

    @Override
    public boolean isValid(AccommodationDetailRequest request, ConstraintValidatorContext context) {
        if (request.checkInDate() == null || request.checkOutDate() == null) {
            return true;
        }
        return !request.checkInDate().isAfter(request.checkOutDate());
    }
}