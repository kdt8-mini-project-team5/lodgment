package com.accommodation.accommodation.domain.booking.facade;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.booking.exception.BookingException;
import com.accommodation.accommodation.domain.booking.exception.errorcode.BookingErrorCode;
import com.accommodation.accommodation.domain.booking.model.request.CreateBookingRequest;
import com.accommodation.accommodation.domain.booking.service.BookingService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookingLockFacade {

    private final RedissonClient redissonClient;
    private final BookingService bookingService;

    public ResponseEntity createBooking (
        CustomUserDetails customUserDetails,
        CreateBookingRequest request
    ) {
        var lock = redissonClient.getLock(request.roomId().toString());

        try {
            boolean avaliable = lock.tryLock(1, 1, TimeUnit.SECONDS);

            if(!avaliable) {
                // lock 획득 실패
                throw new BookingException(BookingErrorCode.WAIT);
            }

            return bookingService.createBooking(customUserDetails, request);

        } catch (InterruptedException e) {
            throw new BookingException(BookingErrorCode.CONFLICT_BOOKING);
        } finally {
            lock.unlock();
        }

    }
}
