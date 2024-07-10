package com.accommodation.accommodation.domain.booking.facade;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.booking.exception.BookingException;
import com.accommodation.accommodation.domain.booking.exception.errorcode.BookingErrorCode;
import com.accommodation.accommodation.domain.booking.model.dto.CartToBookingDTO;
import com.accommodation.accommodation.domain.booking.model.request.CreateBookingRequest;
import com.accommodation.accommodation.domain.booking.model.response.CreateBookingsResponse;
import com.accommodation.accommodation.domain.booking.service.BookingService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookingLockFacade {

    private static final long LOCK_WAIT_TIME = 1;
    private static final long LOCK_LEASE_TIME = 1;

    private final RedissonClient redissonClient;
    private final BookingService bookingService;

    public ResponseEntity createBooking (
        CustomUserDetails customUserDetails,
        CreateBookingRequest request
    ) {
        RLock lock = redissonClient.getLock(request.roomId().toString());

        try {
            boolean avaliable = lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS);

            if(!avaliable) {
                throw new BookingException(BookingErrorCode.WAIT);
            }

            return bookingService.createBooking(customUserDetails, request);

        } catch (InterruptedException e) {
            throw new BookingException(BookingErrorCode.CONFLICT_BOOKING);
        } finally {
            lock.unlock();
        }
    }

    public CreateBookingsResponse.BookingResult createBookings (
        CartToBookingDTO cart, String guestName, String guestTel
    ) {
        RLock lock = redissonClient.getLock(cart.getRoomId().toString());

        try {
            boolean avaliable = lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS);

            if(!avaliable) {
                throw new BookingException(BookingErrorCode.WAIT);
            }

            return bookingService.createBookingFromCart(cart, guestName, guestTel);

        } catch (InterruptedException e) {
            throw new BookingException(BookingErrorCode.CONFLICT_BOOKING);
        } finally {
            lock.unlock();
        }
    }
}
