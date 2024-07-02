package com.accommodation.accommodation.domain.room.repository;

import com.accommodation.accommodation.domain.accommodation.model.entity.QAccommodation;
import com.accommodation.accommodation.domain.booking.model.dto.BookingRoomDetailsDTO;
import com.accommodation.accommodation.domain.booking.model.dto.QBookingRoomDetailsDTO;
import com.accommodation.accommodation.domain.room.model.entity.QRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Optional;

public class RoomRepositoryImpl implements RoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RoomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<BookingRoomDetailsDTO> findRoomDetailsById(Long roomId) {

        return Optional.ofNullable(
            queryFactory
            .select(new QBookingRoomDetailsDTO(
                QRoom.room.title, QRoom.room.price, QRoom.room.minPeople, QRoom.room.maxPeople,
                QAccommodation.accommodation.checkIn,
                QAccommodation.accommodation.checkOut
            ))
            .from(QRoom.room)
            .join(QRoom.room.accommodation, QAccommodation.accommodation)
            .where(QRoom.room.id.eq(roomId))
            .fetchOne());
    }
}
