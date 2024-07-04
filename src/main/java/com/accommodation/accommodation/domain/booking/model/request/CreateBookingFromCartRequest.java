package com.accommodation.accommodation.domain.booking.model.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateBookingFromCartRequest(
    @NotNull List<Long> cartIdList,
    @NotNull String guestName,
    @NotNull String guestTel
    ) {}
