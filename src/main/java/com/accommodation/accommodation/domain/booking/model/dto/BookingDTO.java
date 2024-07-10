package com.accommodation.accommodation.domain.booking.model.dto;

import com.accommodation.accommodation.global.model.entity.BaseTimeStamp;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookingDTO extends BaseTimeStamp {

    private Long userId;

    private String orderId;

    private String guestName;
    private String guestTel;

    private Long roomId;
    private int people;

    private LocalDateTime checkInDatetime;
    private LocalDateTime checkOutDatetime;
    private long totalPrice;
}
