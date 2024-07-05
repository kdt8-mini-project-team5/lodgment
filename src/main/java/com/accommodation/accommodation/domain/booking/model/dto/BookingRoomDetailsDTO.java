package com.accommodation.accommodation.domain.booking.model.dto;

import java.time.LocalTime;
import lombok.Data;

@Data
public class BookingRoomDetailsDTO {

    private String accommodationTitle;
    private String roomTitle;

    private long price;
    private int minPeople;
    private int maxPeople;

    private LocalTime checkInTime;
    private LocalTime checkOutTime;

}
