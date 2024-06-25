package com.accommodation.accommodation.domain.booking.controller;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.booking.model.request.CreateBookingRequest;
import com.accommodation.accommodation.domain.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity createBooking(
        @Valid @RequestBody CreateBookingRequest createBookingRequest,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return bookingService.createBooking(customUserDetails, createBookingRequest);
    }

    @GetMapping
    public ResponseEntity confirmBooking(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return bookingService.confirmBooking(customUserDetails, page, size);
    }

}
