package com.accommodation.accommodation.domain.cart.repository;

import com.accommodation.accommodation.domain.cart.model.entity.Cart;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query("SELECT c  FROM Cart c join FETCH c.room r JOIN FETCH r.accommodation WHERE c.user.id =:userId ORDER BY c.checkInDateTime ASC")
    List<Cart> findByUserId(Long userId);

    Integer countByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Cart c where c.id in :ids and c.user.id = :userId")
    int deleteAllByIds(List<Long> ids, Long userId);

    Boolean existsByRoomIdAndUserIdAndCheckInDateTimeAndCheckOutDateTime(Long roomId,Long userId, LocalDateTime checkInDateTime, LocalDateTime checkOutDateTime);
}
