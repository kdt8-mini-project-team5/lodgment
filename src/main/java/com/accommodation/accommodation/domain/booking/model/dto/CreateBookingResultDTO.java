package com.accommodation.accommodation.domain.booking.model.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBookingResultDTO {

    private String orderId;

    private String guestName;
    private String guestTel;

    private String accommodationTitle;
    private String roomTitle;
    private String roomImage;

    private int minPeople;
    private int maxPeople;

    private LocalDateTime checkInDatetime;
    private LocalDateTime checkOutDatetime;

    private long totalPrice;
}
