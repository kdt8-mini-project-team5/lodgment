package com.accommodation.accommodation.domain.booking.facade;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.booking.exception.BookingException;
import com.accommodation.accommodation.domain.booking.exception.errorcode.BookingErrorCode;
import com.accommodation.accommodation.domain.booking.model.request.CreateBookingRequest;
import com.accommodation.accommodation.domain.booking.service.BookingService;
import com.accommodation.accommodation.global.model.repository.RedisLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookingLockFacade {

    private final RedisLockRepository redisLockRepository;
    private final BookingService bookingService;

    public ResponseEntity createBooking (
        CustomUserDetails customUserDetails,
        CreateBookingRequest request
    ) throws InterruptedException {

        ResponseEntity response = null;

        // TODO : lock Id를 roomId 로만 해도 괜찮은지?
        long lockId = request.roomId();

        while (!redisLockRepository.lock(lockId)) {
            Thread.sleep(100);
        }

        try {
            response = bookingService.createBooking(customUserDetails, request);
        } finally {
            redisLockRepository.unlock(lockId);
        }

        return response;
    }



}
