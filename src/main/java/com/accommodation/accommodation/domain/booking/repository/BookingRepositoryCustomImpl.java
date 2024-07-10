package com.accommodation.accommodation.domain.booking.repository;

import com.accommodation.accommodation.domain.auth.model.entity.User;
import com.accommodation.accommodation.domain.booking.model.dto.BookingDTO;
import com.accommodation.accommodation.domain.booking.model.entity.Booking;
import com.accommodation.accommodation.domain.room.model.entity.Room;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BookingRepositoryCustomImpl implements BookingRepositoryCustom {

    private final EntityManager entityManager;

    public BookingRepositoryCustomImpl(EntityManager em) {
        this.entityManager = em;
    }

    @Transactional
    @Override
    public long saveBooking(BookingDTO bookingDTO) {

        // 연관 엔티티 설정
        User user = entityManager.getReference(User.class, bookingDTO.getUserId());
        Room room = entityManager.getReference(Room.class, bookingDTO.getRoomId());

        Booking booking = Booking.builder()
            .orderId(bookingDTO.getOrderId())
            .guestName(bookingDTO.getGuestName())
            .guestTel(bookingDTO.getGuestTel())
            .people(bookingDTO.getPeople())
            .checkInDatetime(bookingDTO.getCheckInDatetime())
            .checkOutDatetime(bookingDTO.getCheckOutDatetime())
            .totalPrice(bookingDTO.getTotalPrice())
            .user(user)
            .room(room)
            .build();

        entityManager.persist(booking);

        return booking.getId();
    }
}