package com.accommodation.accommodation.domain.cart.repository;

import com.accommodation.accommodation.domain.cart.model.entity.Cart;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart,Long> {

    Page<Cart> findByUserId(Long userId , Pageable pageable);

    Integer countByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Cart c where c.id in :ids")
    void deleteAllByIds(List<Long> ids);
}