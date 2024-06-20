package com.accommodation.accommodation.domain.booking.model.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmBookingResponse {
    private List<BookingResponse> bookingList;

    @Data
    @Builder
    public static class BookingResponse {
        private String orderId;

        private String accommodationTitle;

        private String roomTitle;
        private String roomImg;

        private int minPeople;
        private int maxPeople;

        private LocalDateTime checkInDatetime;
        private LocalDateTime checkOutDatetime;
    }
}
