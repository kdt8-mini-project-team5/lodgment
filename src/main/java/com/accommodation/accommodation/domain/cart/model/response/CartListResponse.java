package com.accommodation.accommodation.domain.cart.model.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartListResponse {

    private List<CartResponse> cartList;

    @Data
    @Builder
    public static class CartResponse {
        private Long cartId;
        private Long accommodationId;
        private String accommodationTitle;
        private int people;
        private Long roomId;
        private String roomTitle;
        private LocalDateTime checkInDatetime;
        private LocalDateTime checkOutDatetime;
        private int minPeople;
        private int maxPeople;
        private Long totalPrice;
        private String roomImg;

        // 예약 가능 여부
        private Boolean isBooking; //true = 예약 가능 , false = 예약 불가
    }
}
