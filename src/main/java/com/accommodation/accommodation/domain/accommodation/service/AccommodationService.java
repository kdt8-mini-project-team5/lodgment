package com.accommodation.accommodation.domain.accommodation.service;

import com.accommodation.accommodation.domain.accommodation.model.entity.Accommodation;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationResponse;
import com.accommodation.accommodation.domain.accommodation.model.type.Category;
import com.accommodation.accommodation.domain.accommodation.repository.AccommodationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    public List<AccommodationResponse> findByCategory(
        Category category, Long cursorId, Pageable pageable, Long cursorMinPrice) {
        List<Accommodation> accommodationList;
        if(cursorId == null) {
            accommodationList = accommodationRepository.findByCategory(category, pageable);
        }else {
            accommodationList = accommodationRepository.findByCategoryWithCursor(category, cursorId, pageable, cursorMinPrice);
        }
        return accommodationList.stream()
            .map(this::createAccommodationResponse)
            .toList();
    }

    private AccommodationResponse createAccommodationResponse(Accommodation accommodation) {
        if (accommodation.getImages().isEmpty()){
            return AccommodationResponse.builder()
                .id(accommodation.getId())
                .title(accommodation.getTitle())
                .minPrice(accommodation.getMinPrice())
                .region(accommodation.getRegion())
                .build();
        }
        return AccommodationResponse.builder()
            .id(accommodation.getId())
            .title(accommodation.getTitle())
            .minPrice(accommodation.getMinPrice())
            .region(accommodation.getRegion())
            .thumbnailUrl(accommodation.getImages().get(1)) // 0은 대표이미지 1은 썸네일
            .build();
    }

}
