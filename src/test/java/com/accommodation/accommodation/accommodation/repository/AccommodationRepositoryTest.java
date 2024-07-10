package com.accommodation.accommodation.accommodation.repository;

import com.accommodation.accommodation.domain.accommodation.model.entity.Accommodation;
import com.accommodation.accommodation.domain.accommodation.model.type.Category;
import com.accommodation.accommodation.domain.accommodation.repository.AccommodationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AccommodationRepositoryTest {
    @Autowired
    private AccommodationRepository accommodationRepository;

    @BeforeEach
    public void setAccommodationRepository()
    {
        accommodationRepository.deleteAll();

        accommodationRepository.save(Accommodation.builder()
                .title("Test Accommodation 1")
                .category(Category.HOTEL)
                .minPrice(50000L)
                .build()
        );

        accommodationRepository.save(Accommodation.builder()
                .title("Test Accommodation 2")
                .category(Category.HOTEL)
                .minPrice(35000L)
                .build()
        );
    }

    @Test
    @DisplayName("Category와 minPrice 기준으로 숙소 페이징 조회")
    public void testFindByCategoryWithCursor() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Accommodation> accommodations = accommodationRepository
                .findByCategoryWithCursor(
                        Category.HOTEL,
                        Long.MAX_VALUE,
                        pageable,
                        35000L
                );

        assertThat(accommodations).hasSize(2);
    }
}
