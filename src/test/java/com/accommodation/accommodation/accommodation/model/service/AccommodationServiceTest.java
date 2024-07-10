package com.accommodation.accommodation.accommodation.model.service;

import com.accommodation.accommodation.domain.accommodation.exception.AccommodationException;
import com.accommodation.accommodation.domain.accommodation.exception.errorcode.AccommodationErrorCode;
import com.accommodation.accommodation.domain.accommodation.model.entity.Accommodation;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationDetailResponse;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationsResponse;
import com.accommodation.accommodation.domain.accommodation.model.type.Category;
import com.accommodation.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.accommodation.accommodation.domain.accommodation.service.AccommodationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
public class AccommodationServiceTest {

    @MockBean
    private AccommodationRepository accommodationRepository;

    private AccommodationService accommodationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        accommodationService = new AccommodationService(accommodationRepository);
    }

    @Test
    @DisplayName("Category와 minPrice 기준으로 숙소 페이징 조회")
    public void testFindByCategoryWithCursor() {
        Pageable pageable = PageRequest.of(0, 2);

        List<Accommodation> accommodationList = Arrays.asList(
                Accommodation.builder()
                        .id(1L)
                        .title("Test Accommodation 1")
                        .category(Category.HOTEL)
                        .minPrice(50000L)
                        .images(Collections.emptyList())
                        .build(),
                Accommodation.builder()
                        .id(2L)
                        .title("Test Accommodation 2")
                        .category(Category.HOTEL)
                        .minPrice(35000L)
                        .images(Collections.emptyList())
                        .build()
        );

        Page<Accommodation> page = new PageImpl<>(accommodationList, pageable, accommodationList.size());

        when(accommodationRepository.findByCategory(any(), any(Pageable.class))).thenReturn(page);
        when(accommodationRepository.findByCategoryWithCursor(any(), anyLong(), any(), anyLong())).thenReturn(page);

        AccommodationsResponse response = accommodationService.findByCategory(Category.HOTEL, null, pageable, 35000L);

        assertThat(response).isNotNull();
        assertThat(response.getAccommodationSimpleResponseList()).hasSize(2);
        assertThat(response.getAccommodationSimpleResponseList().get(0).getTitle()).isEqualTo("Test Accommodation 1");
        assertThat(response.getAccommodationSimpleResponseList().get(1).getTitle()).isEqualTo("Test Accommodation 2");
        assertThat(response.getNextCursorId()).isEqualTo(2L);
        assertThat(response.getNextCursorMinPrice()).isEqualTo(35000L);
    }

    @Test
    @DisplayName("AccommodationService 생성자 테스트")
    public void testAccommodationServiceConstructor() {
        assertThat(accommodationService).isNotNull();
    }

    @Test
    @DisplayName("유효한 ID로 숙소 조회")
    public void testGetAccommodationById_ValidId() {
        Long validId = 1L;
        Accommodation accommodation = Accommodation.builder()
                .id(validId)
                .title("Test Accommodation")
                .category(Category.HOTEL)
                .minPrice(50000L)
                .longitude("127.0")
                .latitude("37.5")
                .checkIn(LocalTime.of(14, 0))
                .checkOut(LocalTime.of(11, 0))
                .images(Collections.emptyList())
                .roomList(Collections.emptyList())
                .build();

        when(accommodationRepository.findById(validId)).thenReturn(Optional.of(accommodation));

        AccommodationDetailResponse response = accommodationService.getAccommodationById(validId, null, null);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Test Accommodation");
    }

    @Test
    @DisplayName("존재하지 않는 ID로 숙소 조회")
    public void testGetAccommodationById_InvalidId() {
        Long invalidId = 999L;

        when(accommodationRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accommodationService.getAccommodationById(invalidId, null, null))
                .isInstanceOf(AccommodationException.class)
                .hasMessage(AccommodationErrorCode.NOT_FOUND.getStatusText());
    }

    @Test
    @DisplayName("잘못된 체크인 및 체크아웃 날짜로 숙소 조회")
    public void testGetAccommodationById_InvalidDates() {
        Long validId = 1L;
        LocalDate checkInDate = LocalDate.now().plusDays(1);
        LocalDate checkOutDate = LocalDate.now();

        Accommodation accommodation = Accommodation.builder()
                .id(validId)
                .title("Test Accommodation")
                .category(Category.HOTEL)
                .minPrice(50000L)
                .longitude("127.0")
                .latitude("37.5")
                .checkIn(LocalTime.of(14, 0))
                .checkOut(LocalTime.of(11, 0))
                .images(Collections.emptyList())
                .roomList(Collections.emptyList())
                .build();

        when(accommodationRepository.findById(validId)).thenReturn(Optional.of(accommodation));

        assertThatThrownBy(() -> accommodationService.getAccommodationById(validId, checkInDate, checkOutDate))
                .isInstanceOf(AccommodationException.class)
                .hasMessage(AccommodationErrorCode.INVALID_DATE.getStatusText());
    }
}