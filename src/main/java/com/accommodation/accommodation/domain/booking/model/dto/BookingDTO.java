package com.accommodation.accommodation.domain.booking.model.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;



@Getter
@Builder
public class BookingDTO {

    private Long userId;

    private String orderId;

    private Long roomId;
    private int people;
    private LocalDateTime checkInDatetime;
    private LocalDateTime checkOutDatetime;
    private long totalPrice;

}
