package com.accommodation.accommodation.domain.accommodation.model.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccommodationsResponse {

    private boolean nextData;

    private Long nextCursorId;

    private Long nextCursorMinPrice;

    private List<AccommodationSimpleResponse> accommodationSimpleResponseList;
}
