package com.accommodation.accommodation.domain.booking.model.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBookingsResponse {

    private List<BookingResult> items;

    @Data
    @Builder
    public static class BookingResult {
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

}
