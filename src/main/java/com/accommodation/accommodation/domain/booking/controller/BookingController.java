package com.accommodation.accommodation.domain.booking.controller;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.booking.exception.BookingException;
import com.accommodation.accommodation.domain.booking.exception.errorcode.BookingErrorCode;
import com.accommodation.accommodation.domain.booking.facade.BookingLockFacade;
import com.accommodation.accommodation.domain.booking.model.request.CreateBookingRequest;
import com.accommodation.accommodation.domain.booking.service.BookingService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    private final BookingLockFacade bookingLockFacade;

    @PostMapping
    public ResponseEntity createBooking(
        @Valid @RequestBody CreateBookingRequest createBookingRequest,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        //return bookingService.createBooking(customUserDetails, createBookingRequest); // Redisson 적용 전
        return bookingLockFacade.createBooking(customUserDetails, createBookingRequest);


        // TODO : parallelStream 적용 여부
        /*
        List<ResponseEntity<?>> responses = createBookingRequests.stream()
            .map(request -> {
                return bookingLockFacade.createBooking(customUserDetails, request);
            }).collect(Collectors.toList());

        return ResponseEntity.ok(responses);
         */
    }

    @GetMapping
    public ResponseEntity confirmBooking(
        @RequestParam(name = "page",defaultValue = "0") int page,
        @RequestParam(name="size",defaultValue = "10") int size,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return bookingService.confirmBooking(customUserDetails, page, size);
    }

}
