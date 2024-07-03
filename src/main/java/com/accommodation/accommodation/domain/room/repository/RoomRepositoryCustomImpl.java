package com.accommodation.accommodation.domain.room.repository;

import com.accommodation.accommodation.domain.accommodation.model.entity.QAccommodation;
import com.accommodation.accommodation.domain.booking.model.dto.BookingRoomDetailsDTO;
import com.accommodation.accommodation.domain.room.model.entity.QRoom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepositoryCustomImpl implements RoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QRoom room = QRoom.room;
    private final QAccommodation accommodation = QAccommodation.accommodation;

    public RoomRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<BookingRoomDetailsDTO> findRoomDetailsById(Long roomId) {

        BookingRoomDetailsDTO result = queryFactory
            .select(Projections.fields(BookingRoomDetailsDTO.class,
                room.title.as("roomTitle"),
                room.price,
                room.minPeople,
                room.maxPeople,
                accommodation.title.as("accommodationTitle"),
                accommodation.checkIn.as("checkInTime"),
                accommodation.checkOut.as("checkOutTime")))
            .from(room)
            .innerJoin(room.accommodation, accommodation)
            .where(room.id.eq(roomId))
            .fetchOne();

        return Optional.ofNullable(result);
    }
}
