package com.accommodation.accommodation.global.validation.type;

import com.accommodation.accommodation.global.validation.DateRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
public @interface DateRangeValidation {

    String message() default "Invalid check-out date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public class DateRange {
    }
}