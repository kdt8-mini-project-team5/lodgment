package com.accommodation.accommodation.domain.accommodation.controller;

import com.accommodation.accommodation.domain.accommodation.model.request.AccommodationListRequest;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationsResponse;
import com.accommodation.accommodation.domain.accommodation.model.type.Category;
import com.accommodation.accommodation.domain.accommodation.service.AccommodationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accommodation")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping()
    public ResponseEntity findAll(
        @ModelAttribute @Valid AccommodationListRequest accommodationListRequest
    ){
        AccommodationsResponse response = accommodationService.findByCategory(
            Category.valueOfType(accommodationListRequest.category()),
            accommodationListRequest.cursorId(),
            PageRequest.of(0, accommodationListRequest.size()),
            accommodationListRequest.cursorMinPrice());
        return ResponseEntity.ok(response);
    }

/*    @GetMapping()
    public ResponseEntity findAll(
        @ModelAttribute @Valid AccommodationListRequest accommodationListRequest
        ){
        Page<AccommodationSimpleResponse> accommodationSimpleResponseList = accommodationService.findByCategory(
            Category.valueOfType(accommodationListRequest.category()),
            accommodationListRequest.cursorId(),
            PageRequest.of(0, accommodationListRequest.size()),
            accommodationListRequest.cursorMinPrice());
        return ResponseEntity.ok(accommodationSimpleResponseList);
    }*/
}
