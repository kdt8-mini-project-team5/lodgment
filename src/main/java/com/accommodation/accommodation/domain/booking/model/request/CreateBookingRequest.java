package com.accommodation.accommodation.domain.booking.model.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record CreateBookingRequest (
    @NotNull Long roomId,
    @Positive int numPeople,
    @NotNull LocalDate checkInDate,
    @NotNull @Future LocalDate checkOutDate
) {}
