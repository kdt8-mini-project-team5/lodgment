package com.accommodation.accommodation.domain.accommodation.model.response;

import lombok.Data;

@Data
public class AccommodationSimpleDTO {

    private Long id;

    private String title;

    private Long minPrice;

    private String region;

    public AccommodationSimpleDTO(Long id, String title, Long minPrice, String region) {
        this.id = id;
        this.title = title;
        this.minPrice = minPrice;
        this.region = region;
    }
}
