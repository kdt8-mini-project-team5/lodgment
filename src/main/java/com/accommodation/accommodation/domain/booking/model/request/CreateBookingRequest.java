package com.accommodation.accommodation.domain.booking.model.request;

import com.accommodation.accommodation.global.validation.type.CheckInCheckOutDateValidation;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@CheckInCheckOutDateValidation(checkInDate = "checkInDate",checkOutDate = "checkOutDate")
public record CreateBookingRequest (
    @NotNull Long roomId,
    @Positive int numPeople,
    @NotNull @FutureOrPresent LocalDate checkInDate,
    @NotNull @Future LocalDate checkOutDate
) {}
