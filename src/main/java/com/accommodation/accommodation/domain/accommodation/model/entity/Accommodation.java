package com.accommodation.accommodation.domain.accommodation.model.entity;

import com.accommodation.accommodation.domain.accommodation.model.type.Category;
import com.accommodation.accommodation.domain.interest.model.entity.Interest;
import com.accommodation.accommodation.domain.room.model.entity.Room;
import com.accommodation.accommodation.global.model.entity.BaseTimeStamp;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Accommodation extends BaseTimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String title;

    @Column
    private String info;

    @Column
    private String address;

    @Column
    private String region;

    @Column
    private String longitude;

    @Column
    private String latitude;

    @Column
    private String tel;

    @Enumerated(EnumType.STRING)
    private Category category; // TODO : enum으로

    @Column
    private LocalTime checkIn;

    @Column
    private LocalTime checkOut;

    @Column
    private boolean parkingLodging; // parking 이 어떨지..

    @Column
    private boolean tv;

    @Column
    private boolean pc;

    @Column
    private boolean internet;

    @Column
    private boolean refrigerator;

    @Column
    private boolean shower;

    @Column
    private boolean aircone;

    @Column
    private boolean chkcooking;

    @Column
    private boolean toiletries;

    @Column
    private boolean kitchenware;

    @Column
    private boolean dryer;

    @Column
    private Long minPrice;

    @BatchSize(size = 100)
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> images;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "accommodation")
    private List<Room> roomList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "accommodation")
    private List<Interest> interestList;


}