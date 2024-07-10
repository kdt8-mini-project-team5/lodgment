package com.accommodation.accommodation.accommodation.model.response;

import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class AccommodationDetailResponseTest {
    private AccommodationDetailResponse.RoomResponse roomResponse;
    List<AccommodationDetailResponse.RoomResponse> rooms = null;

    @Test
    @DisplayName("RoomResponse 생성 테스트")
    public void testRoomResponseCreation() {
        roomResponse = AccommodationDetailResponse.RoomResponse
                .builder()
                .roomId(1L)
                .title("Test Room")
                .price(100000)
                .minPeople(2)
                .maxPeople(3)
                .img(Arrays.asList("test1.jpg", "test2.jpg"))
                .build();
        rooms = Arrays.asList(roomResponse);
    }
    
    @Test
    @DisplayName("AccommodationDetailResponse 생성 테스트")
    public void testAccommodationDetailCreation() {
        AccommodationDetailResponse accommodationDetailResponse = AccommodationDetailResponse.builder()
                .longitude(98.2341)
                .latitude(21.2345)
                .title("Test Hotel")
                .info("Test hotel contents")
                .price(78000)
                .checkIn("15:00")
                .checkOut("11:00")
                .shower(true)
                .aircone(true)
                .tv(true)
                .pc(false)
                .internet(true)
                .refrigerator(true)
                .toiletries(true)
                .kitchenware(true)
                .parkingLodging(true)
                .address("Test address 123")
                .tel("010-1111-1234")
                .dryer(true)
                .roomCount(2)
                .img(Arrays.asList("testImg1.jpg" , "testImg2.jpg"))
                .room(rooms)
                .build();
    }

    // Getter
    private Object getField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private AccommodationDetailResponse createAccommodationDetailResponse(List<AccommodationDetailResponse.RoomResponse> rooms) {
        return AccommodationDetailResponse.builder()
                .longitude(127.02758)
                .latitude(37.49794)
                .title("Test Hotel")
                .info("Test hotel contents")
                .price(50000)
                .checkIn("15:00")
                .checkOut("11:00")
                .shower(true)
                .aircone(true)
                .tv(true)
                .pc(false)
                .internet(true)
                .refrigerator(true)
                .toiletries(true)
                .kitchenware(false)
                .parkingLodging(true)
                .address("123 Test Street")
                .tel("123-456-7890")
                .dryer(true)
                .roomCount(2)
                .img(Arrays.asList("hotel1.jpg", "hotel2.jpg"))
                .room(rooms)
                .build();
    }

    @Test
    @DisplayName("AccommodationDetailResponse 검증 테스트 ")
    public void testAccommodationDetailResponseAssertion(){
        AccommodationDetailResponse response = createAccommodationDetailResponse(rooms);
        assertThat(getField(response, "longitude")).isEqualTo(127.02758);
        assertThat(getField(response, "latitude")).isEqualTo(37.49794);
        assertThat(getField(response, "title")).isEqualTo("Test Hotel");
        assertThat(getField(response, "info")).isEqualTo("Test hotel contents");
        assertThat(getField(response, "price")).isEqualTo(50000L);
        assertThat(getField(response, "checkIn")).isEqualTo("15:00");
        assertThat(getField(response, "checkOut")).isEqualTo("11:00");
        assertThat(getField(response, "shower")).isEqualTo(true);
        assertThat(getField(response, "aircone")).isEqualTo(true);
        assertThat(getField(response, "tv")).isEqualTo(true);
        assertThat(getField(response, "pc")).isEqualTo(false);
        assertThat(getField(response, "internet")).isEqualTo(true);
        assertThat(getField(response, "refrigerator")).isEqualTo(true);
        assertThat(getField(response, "toiletries")).isEqualTo(true);
        assertThat(getField(response, "kitchenware")).isEqualTo(false);
        assertThat(getField(response, "parkingLodging")).isEqualTo(true);
        assertThat(getField(response, "address")).isEqualTo("123 Test Street");
        assertThat(getField(response, "tel")).isEqualTo("123-456-7890");
        assertThat(getField(response, "dryer")).isEqualTo(true);
        assertThat(getField(response, "roomCount")).isEqualTo(2);
        assertThat(getField(response, "img")).isEqualTo(Arrays.asList("hotel1.jpg", "hotel2.jpg"));
    }

    private AccommodationDetailResponse.RoomResponse createRoomResponse() {
        return AccommodationDetailResponse.RoomResponse.builder()
                .roomId(1L)
                .title("Room 1")
                .price(10000)
                .minPeople(2)
                .maxPeople(4)
                .img(Arrays.asList("img1.jpg", "img2.jpg"))
                .build();
    }

    @Test
    @DisplayName("AccommodationDetailResponse 검증 테스트 ")
    public void testAccommodationDetailResponse(){
        AccommodationDetailResponse.RoomResponse room = createRoomResponse();
        assertThat(getField(room, "roomId")).isEqualTo(1L);
        assertThat(getField(room, "title")).isEqualTo("Room 1");
        assertThat(getField(room, "price")).isEqualTo(10000L);
        assertThat(getField(room, "minPeople")).isEqualTo(2);
        assertThat(getField(room, "maxPeople")).isEqualTo(4);
        assertThat(getField(room, "img")).isEqualTo(Arrays.asList("img1.jpg", "img2.jpg"));
    }
}
