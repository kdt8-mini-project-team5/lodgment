package com.accommodation.accommodation.accommodation.model.response;

import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationSimpleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccommodationSimpleResponseTest {
    @Test
    @DisplayName("AccommodationSimpleResponse 생성 테스트")
    public void testAccommodationSimpleResponseCreation() {
        AccommodationSimpleResponse response = AccommodationSimpleResponse.builder()
                .id(1L)
                .title("Test Title")
                .minPrice(50000L)
                .region("Seoul")
                .thumbnailUrl("http://example.com/thumbnail.jpg")
                .build();

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Test Title");
        assertThat(response.getMinPrice()).isEqualTo(50000L);
        assertThat(response.getRegion()).isEqualTo("Seoul");
        assertThat(response.getThumbnailUrl()).isEqualTo("http://example.com/thumbnail.jpg");
    }

    @Test
    @DisplayName("AccommodationSimpleResponse 필드 값 설정 테스트")
    public void testAccommodationSimpleResponseFieldValues() {
        AccommodationSimpleResponse response = AccommodationSimpleResponse.builder()
                .id(2L)
                .title("Another Title")
                .minPrice(60000L)
                .region("Busan")
                .thumbnailUrl("http://example.com/another-thumbnail.jpg")
                .build();

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getTitle()).isEqualTo("Another Title");
        assertThat(response.getMinPrice()).isEqualTo(60000L);
        assertThat(response.getRegion()).isEqualTo("Busan");
        assertThat(response.getThumbnailUrl()).isEqualTo("http://example.com/another-thumbnail.jpg");
    } 
}
