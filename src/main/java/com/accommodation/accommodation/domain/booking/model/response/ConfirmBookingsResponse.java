package com.accommodation.accommodation.domain.booking.model.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmBookingsResponse {

    private List<BookingResponse> bookingList;

    private long totalElements;

    @Data
    @Builder
    public static class BookingResponse {

        private String orderId;

        private String guestName;
        private String guestTel;

        private String accommodationTitle;

        private String roomTitle;
        private String roomImg;

        private int minPeople;
        private int maxPeople;

        private LocalDateTime checkInDatetime;
        private LocalDateTime checkOutDatetime;

        private long totalPrice;
    }
}
