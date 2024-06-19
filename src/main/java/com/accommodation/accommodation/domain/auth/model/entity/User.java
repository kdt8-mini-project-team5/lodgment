package com.accommodation.accommodation.domain.auth.model.entity;

import com.accommodation.accommodation.domain.booking.model.entity.Booking;
import com.accommodation.accommodation.domain.interest.model.entity.Interest;
import com.accommodation.accommodation.global.model.entity.BaseTimeStamp;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
//@NoArgsConstructor
public class User extends BaseTimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Booking> bookingList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Interest> interestList;

}
