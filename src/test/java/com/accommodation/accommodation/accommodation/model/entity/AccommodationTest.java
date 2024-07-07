package com.accommodation.accommodation.accommodation.model.entity;

import com.accommodation.accommodation.domain.accommodation.model.entity.Accommodation;
import com.accommodation.accommodation.domain.accommodation.model.type.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class AccommodationTest
{
    private Accommodation accommodation;

    @BeforeEach
    public void setAccommodation(){
        accommodation = Accommodation.builder()
                .id(1L)
                .title("Test Title")
                .info("Test Info")
                .address("Test Address")
                .region("Test Region")
                .longitude("123.456")
                .latitude("78.910")
                .tel("123-456-7890")
                .category(Category.HOTEL)
                .checkIn(LocalTime.of(14, 0))
                .checkOut(LocalTime.of(11, 0))
                .parkingLodging(true)
                .tv(true)
                .pc(false)
                .internet(true)
                .refrigerator(true)
                .shower(true)
                .aircone(false)
                .chkcooking(false)
                .toiletries(true)
                .kitchenware(false)
                .dryer(true)
                .minPrice(10000L)
                .images(List.of("image_test1.jpg", "image_test2.jpg"))
                .build();
    }

    @Test
    @DisplayName("숙소 등록 테스트")
    public void accommodationCreationTest(){
        assertNotNull(accommodation);
        assertEquals(1L, accommodation.getId());
        assertEquals(1L, accommodation.getId());
        assertEquals("Test Title", accommodation.getTitle());
        assertEquals("Test Info", accommodation.getInfo());
        assertEquals("Test Address", accommodation.getAddress());
        assertEquals("Test Region", accommodation.getRegion());
        assertEquals("123.456", accommodation.getLongitude());
        assertEquals("78.910", accommodation.getLatitude());
        assertEquals("123-456-7890", accommodation.getTel());
        assertEquals(Category.HOTEL, accommodation.getCategory());
        assertEquals(LocalTime.of(14, 0), accommodation.getCheckIn());
        assertEquals(LocalTime.of(11, 0), accommodation.getCheckOut());
        assertTrue(accommodation.isParkingLodging());
        assertTrue(accommodation.isTv());
        assertFalse(accommodation.isPc());
        assertTrue(accommodation.isInternet());
        assertTrue(accommodation.isRefrigerator());
        assertTrue(accommodation.isShower());
        assertFalse(accommodation.isAircone());
        assertFalse(accommodation.isChkcooking());
        assertTrue(accommodation.isToiletries());
        assertFalse(accommodation.isKitchenware());
        assertTrue(accommodation.isDryer());
    }

    @Test
    @DisplayName("숙소 제목 길이 유효성 테스트")
    public void accommodationTitleLengthLimitTest(){
        int maxLength = 50;
        assertThat(accommodation.getTitle().length())
                .as("제목은 50자 이하로 작성하여 주시길 바랍니다." + maxLength)
                .isLessThanOrEqualTo(maxLength);
    }
}
