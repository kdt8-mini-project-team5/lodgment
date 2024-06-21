package com.accommodation.accommodation.domain.room.model.entity;

import com.accommodation.accommodation.domain.accommodation.model.entity.Accommodation;
import com.accommodation.accommodation.domain.auth.model.entity.User;
import com.accommodation.accommodation.domain.booking.model.entity.Booking;
import com.accommodation.accommodation.global.model.entity.BaseTimeStamp;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room extends BaseTimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private Long price;

    @Column
    private int minPeople;

    @Column
    private int maxPeople;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> images;

    @ManyToOne(fetch = FetchType.LAZY)
    private Accommodation accommodation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
    private List<Booking> bookingList;
}
