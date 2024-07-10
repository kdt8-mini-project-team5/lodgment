package com.accommodation.accommodation.domain.room.repository;

import com.accommodation.accommodation.domain.booking.model.dto.BookingRoomDetailsDTO;
import java.util.Optional;

public interface RoomRepositoryCustom {

    Optional<BookingRoomDetailsDTO> findRoomDetailsById(Long roomId);

}
