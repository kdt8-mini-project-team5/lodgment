package com.accommodation.accommodation.domain.booking.repository;

import com.accommodation.accommodation.domain.booking.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}