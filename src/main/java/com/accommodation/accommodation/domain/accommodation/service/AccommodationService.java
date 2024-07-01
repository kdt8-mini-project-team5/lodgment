package com.accommodation.accommodation.domain.accommodation.service;

import com.accommodation.accommodation.domain.accommodation.exception.AccommodationException;
import com.accommodation.accommodation.domain.accommodation.exception.errorcode.AccommodationErrorCode;
import com.accommodation.accommodation.domain.accommodation.model.entity.Accommodation;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationSimpleDTO;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationSimpleResponse;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationsResponse;
import com.accommodation.accommodation.domain.accommodation.model.type.Category;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationDetailResponse;
import com.accommodation.accommodation.domain.accommodation.repository.AccommodationRepository;

import com.accommodation.accommodation.domain.room.model.entity.Room;
import com.accommodation.accommodation.domain.room.model.response.RoomResponse;
import com.accommodation.accommodation.domain.room.repository.RoomRepository;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;

    @Cacheable(cacheNames = "accommodationList", key = "#category.name() + ':' + #cursorId + ':' + #cursorMinPrice")
    @Transactional(readOnly = true)
    public AccommodationsResponse findByCategory(Category category, Long cursorId, Pageable pageable, Long cursorMinPrice) {
        Page<AccommodationSimpleDTO> accommodationPage;
        if (cursorId == null) {
            accommodationPage = accommodationRepository.findByCategory(category, pageable);
        } else {
            accommodationPage = accommodationRepository.findByCategoryWithCursor(category, cursorId, pageable, cursorMinPrice);
        }
        // 숙소데이터 가져와서 응답용으로 변환
        List<AccommodationSimpleResponse> responseList = accommodationPage.stream()
                .map(this::createAccommodationResponse)
                .toList();

        if (responseList.isEmpty()) {
            throw new AccommodationException(AccommodationErrorCode.NOT_FOUND);
        }

        // id 기반 이미지 가져오는 로직
        List<Long> idList = responseList.stream()
                .map(AccommodationSimpleResponse::getId).toList();
        List<List<String>> accommodationImagesByIds = accommodationRepository.findAccommodationImagesByIds(idList);

        for (int i = 0; i < responseList.size(); i++) {
            responseList.get(i).setThumbnailUrl(accommodationImagesByIds.get(accommodationImagesByIds.size() - 1 - (i * 2)).get(0));
        }

        boolean nextData = true;
        if (accommodationPage.getTotalElements() - accommodationPage.getNumberOfElements() == 0) {
            nextData = false;
        }


        return AccommodationsResponse.builder()
                .accommodationSimpleResponseList(responseList)
                .nextData(nextData)
                .nextCursorId(responseList.get(responseList.size() - 1).getId())
                .nextCursorMinPrice(responseList.get(responseList.size() - 1).getMinPrice())
                .build();
    }

    private AccommodationSimpleResponse createAccommodationResponse(AccommodationSimpleDTO accommodationSimpleDTO) {
        return AccommodationSimpleResponse.builder()
                .id(accommodationSimpleDTO.getId())
                .title(accommodationSimpleDTO.getTitle())
                .minPrice(accommodationSimpleDTO.getMinPrice())
                .region(accommodationSimpleDTO.getRegion())
                .build();
    }

//    @Cacheable(cacheNames = "accommodationDetails", key = "#id", unless = "#result == null")
    @Transactional(readOnly = true)
    public AccommodationDetailResponse getAccommodationById(Long id, LocalDate checkInDate, LocalDate checkOutDate) {
        Accommodation accommodation = accommodationRepository.findAccommodationDetailById(id)
                .orElseThrow(() -> new AccommodationException(AccommodationErrorCode.NOT_FOUND));

        if (checkInDate != null && checkOutDate != null && checkInDate.isAfter(checkOutDate)) {
            throw new AccommodationException(AccommodationErrorCode.INVALID_DATE);
        }

        List<Room> roomList = roomRepository.findRoomDetailByAccommodationId(
                accommodation.getId());

        AccommodationDetailResponse response = AccommodationDetailResponse.builder()
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
                .img(new ArrayList<>(accommodation.getImages()))    // 숙소 이미지 리스트 초기화
                .room(roomList.stream()
                        .map(room -> {
                            List<String> initializedRoomImages = new ArrayList<>(room.getImages()); // 방 이미지 리스트 초기화
                            return RoomResponse.builder()
                                    .title(room.getTitle())
                                    .price(room.getPrice())
                                    .minPeople(room.getMinPeople())
                                    .maxPeople(room.getMaxPeople())
                                    .img(initializedRoomImages)
                                    .build();
                        })
                        .toList())
                .roomCount(roomList.size())
                .build();

        return response;
    }

}