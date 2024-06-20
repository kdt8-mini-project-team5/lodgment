package com.accommodation.accommodation.domain.booking.model.request;

import java.time.LocalDate;

public record CreateBookingRequest (

    Long roomId,

    int numPeople,

    LocalDate checkInDate,

    LocalDate checkOutDate
) {}
