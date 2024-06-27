package com.accommodation.accommodation.domain.room.repository;

import com.accommodation.accommodation.domain.room.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {

}