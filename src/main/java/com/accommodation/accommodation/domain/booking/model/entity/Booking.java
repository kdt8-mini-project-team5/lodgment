package com.accommodation.accommodation.domain.booking.model.entity;

import com.accommodation.accommodation.domain.auth.model.entity.User;
import com.accommodation.accommodation.domain.room.model.entity.Room;
import com.accommodation.accommodation.global.model.entity.BaseTimeStamp;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Booking extends BaseTimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime checkInDatetime;

    @Column
    private LocalDateTime checkOutDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

}
