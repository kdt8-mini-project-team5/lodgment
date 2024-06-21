package com.accommodation.accommodation.domain.accommodation.service;

import com.accommodation.accommodation.domain.accommodation.model.entity.Accommodation;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationSimpleResponse;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationsResponse;
import com.accommodation.accommodation.domain.accommodation.model.type.Category;
import com.accommodation.accommodation.domain.accommodation.repository.AccommodationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    public AccommodationsResponse findByCategory(Category category, Long cursorId, Pageable pageable, Long cursorMinPrice) {
        Page<Accommodation> accommodationPage;
        if(cursorId == null) {
            accommodationPage = accommodationRepository.findByCategory(category, pageable);
        }else {
            accommodationPage = accommodationRepository.findByCategoryWithCursor(category, cursorId, pageable, cursorMinPrice);
        }

        List<AccommodationSimpleResponse> list = accommodationPage.stream()
            .map(this::createAccommodationResponse).toList();

        boolean nextData = true;
        if (accommodationPage.getTotalElements()-accommodationPage.getNumberOfElements()==0){
            nextData = false;
        }

        return AccommodationsResponse.builder()
                .accommodationSimpleResponseList(list)
                .nextData(nextData)
                .nextCursorId(list.get(list.size()-1).getId())
                .nextCursorMinPrice(list.get(list.size()-1).getMinPrice())
                .build();
    }

    private AccommodationSimpleResponse createAccommodationResponse(Accommodation accommodation) {
        if (accommodation.getImages().isEmpty()){
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

}
