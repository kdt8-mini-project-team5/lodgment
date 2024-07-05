package com.accommodation.accommodation.domain.booking.service;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.booking.facade.BookingLockFacade;
import com.accommodation.accommodation.domain.booking.model.entity.Booking;
import com.accommodation.accommodation.domain.booking.model.request.CreateBookingRequest;
import com.accommodation.accommodation.domain.booking.repository.BookingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    BookingLockFacade bookingLockFacade;

    @AfterEach
    public void after() {
        bookingRepository.deleteAll();
    }

    @Test
    public void 동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;

        String email = "test1@test.com";
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, email, email);

        long roomId = 1L;
        int numPeople = 2;
        LocalDate checkInDate = LocalDate.of(2024, 07, 28);
        LocalDate checkOutDate = LocalDate.of(2024, 07, 29);

        CreateBookingRequest request = new CreateBookingRequest(
            roomId, numPeople, checkInDate, checkOutDate
        );

        var executorService = Executors.newFixedThreadPool(32);
        var latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    // bookingService.createBooking(customUserDetails, request);
                    bookingLockFacade.createBooking(customUserDetails, request);
                } finally {
                    latch.countDown();
                }
            });

        }

        latch.await();

        long booking = bookingRepository.count();

        assertEquals(1, booking);
    }*/

}