package com.accommodation.accommodation.domain.accommodation.service;

import com.accommodation.accommodation.domain.accommodation.exception.AccommodationException;
import com.accommodation.accommodation.domain.accommodation.exception.errorcode.AccommodationErrorCode;
import com.accommodation.accommodation.domain.accommodation.model.entity.Accommodation;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationResponse;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationResponse.RoomResponse;
import com.accommodation.accommodation.domain.accommodation.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    @Transactional(readOnly = true)
    public AccommodationResponse getAccommodationById(Long id, LocalDate checkInDate, LocalDate checkOutDate) {
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new AccommodationException(AccommodationErrorCode.NOT_FOUND));

        if (checkInDate != null && checkOutDate != null && checkInDate.isAfter(checkOutDate)) {
            throw new AccommodationException(AccommodationErrorCode.INVALID_DATE);
        }

        return AccommodationResponse.builder()
                .longitude(accommodation.getLongitude())
                .latitude(accommodation.getLatitude())
                .title(accommodation.getTitle())
                .info(accommodation.getInfo())
                .price(accommodation.getMinPrice())
                .checkIn(accommodation.getCheck_in())
                .checkOut(accommodation.getCheck_out())
                .shower(accommodation.isShower())
                .aircone(accommodation.isAircone())
                .tv(accommodation.isTv())
                .pc(accommodation.isPc())
                .internet(accommodation.isInternet())
                .refrigerator(accommodation.isRefrigerator())
                .toiletries(accommodation.isToiletries())
                .kitchenware(accommodation.isKitchenware())
                .parkingLodging(accommodation.isParkingLodging())
                .address(accommodation.getAddress())
                .tel(accommodation.getTel())
                .dryer(accommodation.isDryer())
                .roomCount(accommodation.getRoomList().size())
                .img(accommodation.getImages())
                .room(accommodation.getRoomList().stream()
                        .map(room -> RoomResponse.builder()
                                .title(room.getTitle())
                                .price(room.getPrice())
                                .minPeople(Integer.parseInt(room.getMinPeople()))
                                .maxPeople(Integer.parseInt(room.getMaxPeople()))
                                .img(room.getImages())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
