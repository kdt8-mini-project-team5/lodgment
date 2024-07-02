package com.accommodation.accommodation.domain.accommodation.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationsResponse {

    private boolean nextData;

    private Long nextCursorId;

    private Long nextCursorMinPrice;

    private List<AccommodationSimpleResponse> accommodationSimpleResponseList;
}
