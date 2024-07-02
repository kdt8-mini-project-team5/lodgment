package com.accommodation.accommodation.domain.accommodation.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationSimpleResponse {

    private Long id;

    private String title;

    private Long minPrice;

    private String region;

    private String thumbnailUrl;
}
