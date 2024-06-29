package com.accommodation.accommodation.domain.cart.model.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CartRequest(
    @NotNull Long roomId,
    @NotNull int people,
    LocalDate checkInDate,
    LocalDate checkOutDate
) {
}
