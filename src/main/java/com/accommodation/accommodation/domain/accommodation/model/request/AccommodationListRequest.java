package com.accommodation.accommodation.domain.accommodation.model.request;

import com.accommodation.accommodation.global.validation.type.CategoryValidation;
import jakarta.validation.constraints.Max;

public record AccommodationListRequest(
    @CategoryValidation String category,
    Long cursorMinPrice,
    Long cursorId,
    @Max(value = 10, message = "size는 최대 {value}입니다.") int size){
}
