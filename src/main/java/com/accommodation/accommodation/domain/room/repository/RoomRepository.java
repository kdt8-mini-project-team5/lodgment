package com.accommodation.accommodation.domain.room.repository;

import com.accommodation.accommodation.domain.room.model.entity.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> , RoomRepositoryCustom{

    @Query("SELECT r FROM Room r JOIN FETCH r.images where r.accommodation.id = :accommodationId")
    List<Room> findRoomDetailByAccommodationId(Long accommodationId);
}