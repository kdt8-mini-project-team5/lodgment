package com.accommodation.accommodation.domain.accommodation.repository;

import com.accommodation.accommodation.domain.accommodation.model.entity.Accommodation;
import com.accommodation.accommodation.domain.accommodation.model.type.Category;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    @Query("SELECT a FROM Accommodation a WHERE a.category = :category AND (a.minPrice = :minPrice AND a.id < :cursorId) or a.minPrice > :minPrice ORDER BY a.minPrice, a.id DESC") // minPrice 기반 정렬
    Page<Accommodation> findByCategoryWithCursor(Category category,Long cursorId, Pageable pageable, Long minPrice);

    @Query("SELECT a FROM Accommodation a WHERE a.category = :category ORDER BY a.minPrice , a.id DESC") // minPrice 기반 정렬
    Page<Accommodation> findByCategory(Category category, Pageable pageable);

/*    @Query("SELECT a FROM Accommodation a WHERE a.category = :category AND (a.minPrice = :minPrice AND a.id < :cursorId) or a.minPrice > :minPrice ORDER BY a.minPrice, a.id DESC") // minPrice 기반 정렬
    List<Accommodation> findByCategoryWithCursor(Category category,Long cursorId, Pageable pageable, Long minPrice);

    @Query("SELECT a FROM Accommodation a WHERE a.category = :category ORDER BY a.minPrice , a.id DESC") // minPrice 기반 정렬
    List<Accommodation> findByCategory(Category category, Pageable pageable);*/
}