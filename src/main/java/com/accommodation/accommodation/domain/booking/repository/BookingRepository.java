package com.accommodation.accommodation.domain.booking.repository;

import com.accommodation.accommodation.domain.booking.model.entity.Booking;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId AND" +
        "(:checkInDatetime < b.checkOutDatetime AND :checkOutDatetime > b.checkInDatetime)")
    List<Booking> checkConflictingBookings(
        @Param("roomId") Long roomId,
        @Param("checkInDatetime") LocalDateTime checkInDatetime,
        @Param("checkOutDatetime") LocalDateTime checkOutDatetime
    );

    Page<Booking> findAllByUserId(Long userId, Pageable pageable);


}