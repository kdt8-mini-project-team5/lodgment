package com.accommodation.accommodation.domain.cart.model.request;

import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.RequestParam;

public record CartListRequest(
    @RequestParam(required = false, defaultValue = "1") @Min(1) int page,
    @RequestParam(required = false, defaultValue = "10") @Min(1) int size
) {

}
