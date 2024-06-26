package com.accommodation.accommodation.domain.accommodation.service;

import com.accommodation.accommodation.domain.accommodation.exception.AccommodationException;
import com.accommodation.accommodation.domain.accommodation.exception.errorcode.AccommodationErrorCode;
import com.accommodation.accommodation.domain.accommodation.model.entity.Accommodation;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationSimpleResponse;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationsResponse;
import com.accommodation.accommodation.domain.accommodation.model.type.Category;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationDetailResponse;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationDetailResponse.RoomResponse;
import com.accommodation.accommodation.domain.accommodation.repository.AccommodationRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public AccommodationsResponse findByCategory(Category category, Long cursorId, Pageable pageable, Long cursorMinPrice) {
        Page<Accommodation> accommodationPage;
        if (cursorId == null) {
            accommodationPage = accommodationRepository.findByCategory(category, pageable);
        } else {
            accommodationPage = accommodationRepository.findByCategoryWithCursor(category, cursorId, pageable, cursorMinPrice);
        }


        List<AccommodationSimpleResponse> list = accommodationPage.stream()
                .map(this::createAccommodationResponse).toList();

        boolean nextData = true;
        if (accommodationPage.getTotalElements() - accommodationPage.getNumberOfElements() == 0) {
            nextData = false;
        }

        return AccommodationsResponse.builder()
                .accommodationSimpleResponseList(list)
                .nextData(nextData)
                .nextCursorId(list.get(list.size() - 1).getId())
                .nextCursorMinPrice(list.get(list.size() - 1).getMinPrice())
                .build();
    }

    private AccommodationSimpleResponse createAccommodationResponse(Accommodation accommodation) {
        if (accommodation.getImages().isEmpty()) {
            return AccommodationSimpleResponse.builder()
                    .id(accommodation.getId())
                    .title(accommodation.getTitle())
                    .minPrice(accommodation.getMinPrice())
                    .region(accommodation.getRegion())
                    .build();
        }
        return AccommodationSimpleResponse.builder()
                .id(accommodation.getId())
                .title(accommodation.getTitle())
                .minPrice(accommodation.getMinPrice())
                .region(accommodation.getRegion())
                .thumbnailUrl(accommodation.getImages().get(1)) // 0은 대표이미지 1은 썸네일
                .build();
    }

    @Transactional(readOnly = true)
    public AccommodationDetailResponse getAccommodationById(Long id, LocalDate checkInDate, LocalDate checkOutDate) {
        Accommodation accommodation = accommodationRepository.findAccommodationDetailById(id)
                .orElseThrow(() -> new AccommodationException(AccommodationErrorCode.NOT_FOUND));

        if (checkInDate != null && checkOutDate != null && checkInDate.isAfter(checkOutDate)) {
            throw new AccommodationException(AccommodationErrorCode.INVALID_DATE);
        }

        accommodation.getImages().size();
        accommodation.getRoomList().forEach(room -> room.getImages().size());

        return AccommodationDetailResponse.builder()
                .longitude(Double.parseDouble(accommodation.getLongitude()))
                .latitude(Double.parseDouble(accommodation.getLatitude()))
                .title(accommodation.getTitle())
                .info(accommodation.getInfo())
                .price(accommodation.getMinPrice())
                .checkIn(accommodation.getCheckIn().toString())
                .checkOut(accommodation.getCheckOut().toString())
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
                                .minPeople(room.getMinPeople())
                                .maxPeople(room.getMaxPeople())
                                .img(room.getImages())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

}