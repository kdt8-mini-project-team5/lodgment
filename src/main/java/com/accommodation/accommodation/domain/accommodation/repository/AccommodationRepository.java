package com.accommodation.accommodation.domain.accommodation.repository;

import com.accommodation.accommodation.domain.accommodation.model.entity.Accommodation;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationSimpleDTO;
import com.accommodation.accommodation.domain.accommodation.model.type.Category;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    /*    @Query("SELECT new com.accommodation.accommodation.domain.accommodation.model.response.AccommodationSimpleDTO(a.id,a.title,a.minPrice,a.region) "
            + "FROM Accommodation a WHERE a.category = :category AND (a.minPrice = :minPrice AND a.id < :cursorId) or a.minPrice > :minPrice ORDER BY a.minPrice, a.id DESC") // minPrice 기반 정렬
        Page<AccommodationSimpleDTO> findByCategoryWithCursor(Category category,Long cursorId, Pageable pageable, Long minPrice);*/
    @Query(
        "SELECT new com.accommodation.accommodation.domain.accommodation.model.response.AccommodationSimpleDTO(a.id,a.title,a.minPrice,a.region) " +
            "FROM Accommodation a " +
            "WHERE a.category = :category AND a.minPrice = :minPrice AND a.id < :cursorId " +
            "UNION ALL " +
            "SELECT new com.accommodation.accommodation.domain.accommodation.model.response.AccommodationSimpleDTO(a.id,a.title,a.minPrice,a.region) " +
            "FROM Accommodation a " +
            "WHERE a.category = :category AND a.minPrice > :minPrice " +
            "ORDER BY a.minPrice, a.id DESC"
    )
    Page<AccommodationSimpleDTO> findByCategoryWithCursor(Category category,Long cursorId, Pageable pageable, Long minPrice);


    @Query("SELECT new com.accommodation.accommodation.domain.accommodation.model.response.AccommodationSimpleDTO(a.id,a.title,a.minPrice,a.region) "
        + "FROM Accommodation a WHERE a.category = :category ORDER BY a.minPrice , a.id DESC") // minPrice 기반 정렬
    Page<AccommodationSimpleDTO> findByCategory(Category category, Pageable pageable);

    @Query("SELECT new com.accommodation.accommodation.domain.accommodation.model.response.AccommodationSimpleDTO(a.id,a.title,a.minPrice,a.region) "
        + "FROM Accommodation a WHERE a.category = :category and a.minPrice = :minPrice or a.minPrice > :minPrice ORDER BY a.minPrice , a.id DESC") // minPrice 기반 정렬
    Page<AccommodationSimpleDTO> findByCategoryWithCursorMinPrice(Category category,Long minPrice ,Pageable pageable);


    @Query("SELECT a.images FROM Accommodation a WHERE a.id in :ids")
    List<List<String>> findAccommodationImagesByIds(List<Long> ids);

    @Query("SELECT a FROM Accommodation a JOIN FETCH a.images where a.id = :id")
    Optional<Accommodation> findAccommodationDetailById(Long id);

}