package com.accommodation.accommodation.domain.accommodation.controller;

import com.accommodation.accommodation.domain.accommodation.exception.AccommodationException;
import com.accommodation.accommodation.domain.accommodation.exception.errorcode.AccommodationErrorCode;
import com.accommodation.accommodation.domain.accommodation.model.request.AccommodationListRequest;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationsResponse;
import com.accommodation.accommodation.domain.accommodation.model.type.Category;
import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationDetailResponse;
import com.accommodation.accommodation.domain.accommodation.service.AccommodationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accommodation")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping()
    public ResponseEntity findAll(
            @ModelAttribute @Valid AccommodationListRequest accommodationListRequest
    ) {
        AccommodationsResponse response = accommodationService.findByCategory(
                Category.valueOfType(accommodationListRequest.category()),
                accommodationListRequest.cursorId(),
                PageRequest.of(0, accommodationListRequest.size()),
                accommodationListRequest.cursorMinPrice());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccommodationDetailResponse> getAccommodation(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {

        if (checkInDate != null && checkOutDate != null && checkInDate.isAfter(checkOutDate)) {
            throw new AccommodationException(AccommodationErrorCode.INVALID_DATE);
        }

        AccommodationDetailResponse accommodationDetailResponse = accommodationService.getAccommodationById(id, checkInDate, checkOutDate);
        return ResponseEntity.ok(accommodationDetailResponse);
    }
}