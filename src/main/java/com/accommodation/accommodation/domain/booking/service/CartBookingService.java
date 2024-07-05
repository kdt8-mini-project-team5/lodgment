package com.accommodation.accommodation.domain.booking.service;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.booking.facade.BookingLockFacade;
import com.accommodation.accommodation.domain.booking.model.request.CreateBookingFromCartRequest;
import com.accommodation.accommodation.domain.booking.model.response.CreateBookingsResponse;
import com.accommodation.accommodation.domain.cart.service.CartService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartBookingService {

    private final BookingLockFacade bookingLockFacade;
    private final CartService cartService;


    public ResponseEntity createBookingsFromCart(
        CreateBookingFromCartRequest cartRequest, CustomUserDetails customUserDetails) {

        List<CreateBookingsResponse.BookingResult> bookingResults = cartRequest.cartIdList()
            .stream()
            .map(cartService::getCartForBooking)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(cart -> cart.getUserId().equals(customUserDetails.getUserId()))
            .map(cart -> bookingLockFacade.createBookings(cart, cartRequest.guestName(),
                cartRequest.guestTel()))
            .filter(Objects::nonNull)
            .toList();

        CreateBookingsResponse resultResponse = CreateBookingsResponse.builder()
            .items(bookingResults)
            .build();

        return ResponseEntity.ok().body(resultResponse);
    }
}
