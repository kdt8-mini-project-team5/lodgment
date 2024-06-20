package com.accommodation.accommodation.domain.booking.model.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBookingResponse {
    private String orderId;

    private String roomTitle;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

}
