package com.accommodation.accommodation.domain.booking.model.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartToBookingDTO {

    private Long userId;
    private Long roomId;

    private String accommodationTitle;
    private String roomTitle;

    private int minPeople;
    private int maxPeople;

    private int people;

    private long totalPrice;

    private LocalDateTime checkInDatetime;
    private LocalDateTime checkOutDatetime;
}
