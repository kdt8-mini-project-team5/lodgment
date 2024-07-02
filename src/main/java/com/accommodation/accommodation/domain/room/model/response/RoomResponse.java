package com.accommodation.accommodation.domain.room.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
    private Long roomId;
    private String title;
    private long price;
    private int minPeople;
    private int maxPeople;
    private List<String> img;
}