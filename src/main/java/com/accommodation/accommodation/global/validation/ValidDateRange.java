package com.accommodation.accommodation.global.validation;

import java.time.LocalDate;

public class ValidDateRange {

    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;

    public ValidDateRange(LocalDate checkInDate, LocalDate checkOutDate) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
}