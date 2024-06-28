package com.accommodation.accommodation.domain.booking.repository;

import com.accommodation.accommodation.domain.booking.model.dto.BookingDTO;
import com.accommodation.accommodation.domain.booking.model.entity.Booking;

import java.time.LocalDateTime;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "SELECT COUNT(b) FROM Booking b WHERE b.room.id = :roomId " +
            "AND (b.checkInDatetime < :checkOutDatetime AND b.checkOutDatetime > :checkInDatetime) FOR UPDATE",
            nativeQuery = true)
    long checkConflictingBookings(
            @Param("roomId") Long roomId,
            @Param("checkInDatetime") LocalDateTime checkInDatetime,
            @Param("checkOutDatetime") LocalDateTime checkOutDatetime);

    @Modifying
    @Transactional
    @Query(
            value = "INSERT INTO booking (user_id, order_id, room_id, check_in_datetime, check_out_datetime, people, total_price) " +
                    "VALUES (:#{#booking.userId}, :#{#booking.orderId}, :#{#booking.roomId}, :#{#booking.checkInDatetime}, :#{#booking.checkOutDatetime}, :#{#booking.people}, :#{#booking.totalPrice})",
            nativeQuery = true
    )
    int saveBooking(@Param("booking") BookingDTO bookingDTO);


    Page<Booking> findAllByUserId(Long userId, Pageable pageable);
}