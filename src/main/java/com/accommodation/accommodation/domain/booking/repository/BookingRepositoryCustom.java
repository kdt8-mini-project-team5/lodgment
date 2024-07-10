package com.accommodation.accommodation.domain.booking.repository;

import com.accommodation.accommodation.domain.booking.model.dto.BookingDTO;

public interface BookingRepositoryCustom {

    long saveBooking(BookingDTO bookingDTO);

}
