package com.accommodation.accommodation.domain.accommodation.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class AccommodationDetailResponse {
    private String longitude;
    private String latitude;
    private String title;
    private String info;
    private Long price;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private boolean shower;
    private boolean aircone;
    private boolean tv;
    private boolean pc;
    private boolean internet;
    private boolean refrigerator;
    private boolean toiletries;
    private boolean kitchenware;
    private boolean parkingLodging;
    private String address;
    private String tel;
    private boolean dryer;
    private int roomCount;
    private List<String> img;
    private List<RoomResponse> room;

    @Data
    @Builder
    public static class RoomResponse {
        private String title;
        private Long price;
        private int minPeople;
        private int maxPeople;
        private List<String> img;
    }
}