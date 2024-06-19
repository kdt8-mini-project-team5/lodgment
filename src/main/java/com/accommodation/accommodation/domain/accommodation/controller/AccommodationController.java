package com.accommodation.accommodation.domain.accommodation.controller;

import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationResponse;
import com.accommodation.accommodation.domain.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/accommodation")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping("/{id}")
    public ResponseEntity<AccommodationResponse> getAccommodation(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {
        AccommodationResponse accommodationResponse = accommodationService.getAccommodationById(id, checkInDate, checkOutDate);
        return ResponseEntity.ok(accommodationResponse);
    }
}