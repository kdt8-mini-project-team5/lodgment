package com.accommodation.accommodation.domain.room.repository;

import com.accommodation.accommodation.domain.accommodation.model.entity.Accommodation;
import com.accommodation.accommodation.domain.room.model.entity.Room;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Cacheable(cacheNames = "room", key = "#accommodationId")
    @Query("SELECT r FROM Room r JOIN FETCH r.images where r.accommodation.id = :accommodationId")
    List<Room> findRoomDetailByAccommodationId(Long accommodationId);
}