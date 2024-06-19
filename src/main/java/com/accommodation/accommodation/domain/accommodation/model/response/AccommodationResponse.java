package com.accommodation.accommodation.domain.accommodation.model.response;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AccommodationResponse {

    private Long id;
    private String title;
    private Long minPrice;
    private String region;
    private String thumbnailUrl;
}
