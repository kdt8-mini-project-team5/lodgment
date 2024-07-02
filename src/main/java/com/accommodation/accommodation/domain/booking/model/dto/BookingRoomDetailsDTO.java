package com.accommodation.accommodation.domain.booking.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class BookingRoomDetailsDTO {

    private String title;
    private long price;
    private int minPeople;
    private int maxPeople;

    private LocalTime checkIn;
    private LocalTime checkOut;

    @QueryProjection
    public BookingRoomDetailsDTO(String title, long price, int minPeople, int maxPeople,
        LocalTime checkIn, LocalTime checkOut) {
        this.title = title;
        this.price = price;
        this.minPeople = minPeople;
        this.maxPeople = maxPeople;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

}
