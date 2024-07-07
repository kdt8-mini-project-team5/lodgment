package com.accommodation.accommodation.accommodation.model.response;

import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationSimpleResponse;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AccommodationsResponseTest {
    @Test
    @DisplayName("AccommodationsResponse 생성 테스트")
    public void testAccommodationsResponseCreation() {
        AccommodationSimpleResponse simpleResponse1 = AccommodationSimpleResponse.builder()
                .id(1L)
                .title("Hotel A")
                .minPrice(50000L)
                .region("Seoul")
                .thumbnailUrl("http://example.com/hotel-a.jpg")
                .build();

        AccommodationSimpleResponse simpleResponse2 = AccommodationSimpleResponse.builder()
                .id(2L)
                .title("Hotel B")
                .minPrice(60000L)
                .region("Busan")
                .thumbnailUrl("http://example.com/hotel-b.jpg")
                .build();

        AccommodationsResponse response = AccommodationsResponse.builder()
                .nextData(true)
                .nextCursorId(3L)
                .nextCursorMinPrice(70000L)
                .accommodationSimpleResponseList(Arrays.asList(simpleResponse1, simpleResponse2))
                .build();

        assertThat(response).isNotNull();
        assertThat(response.isNextData()).isTrue();
        assertThat(response.getNextCursorId()).isEqualTo(3L);
        assertThat(response.getNextCursorMinPrice()).isEqualTo(70000L);

        List<AccommodationSimpleResponse> simpleResponses = response.getAccommodationSimpleResponseList();
        assertThat(simpleResponses).hasSize(2);

        AccommodationSimpleResponse response1 = simpleResponses.get(0);
        assertThat(response1.getId()).isEqualTo(1L);
        assertThat(response1.getTitle()).isEqualTo("Hotel A");
        assertThat(response1.getMinPrice()).isEqualTo(50000L);
        assertThat(response1.getRegion()).isEqualTo("Seoul");
        assertThat(response1.getThumbnailUrl()).isEqualTo("http://example.com/hotel_a.jpg");

        AccommodationSimpleResponse response2 = simpleResponses.get(1);
        assertThat(response2.getId()).isEqualTo(2L);
        assertThat(response2.getTitle()).isEqualTo("Hotel B");
        assertThat(response2.getMinPrice()).isEqualTo(60000L);
        assertThat(response2.getRegion()).isEqualTo("Busan");
        assertThat(response2.getThumbnailUrl()).isEqualTo("http://example.com/hotel_b.jpg");
    }
}
