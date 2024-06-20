package com.accommodation.accommodation.domain.booking.service;

import com.accommodation.accommodation.domain.auth.repository.UserRepository;
import com.accommodation.accommodation.domain.booking.exception.BookingException;
import com.accommodation.accommodation.domain.booking.exception.errorcode.BookingErrorCode;
import com.accommodation.accommodation.domain.booking.model.entity.Booking;
import com.accommodation.accommodation.domain.booking.model.request.CreateBookingRequest;
import com.accommodation.accommodation.domain.booking.model.response.ConfirmBookingResponse;
import com.accommodation.accommodation.domain.booking.model.response.ConfirmBookingResponse.BookingResponse;
import com.accommodation.accommodation.domain.booking.model.response.CreateBookingResponse;
import com.accommodation.accommodation.domain.booking.repository.BookingRepository;
import com.accommodation.accommodation.domain.room.repository.RoomRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    // temporary for test
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity createBooking(CreateBookingRequest request) {

        var roomEntity = roomRepository.findById(request.roomId())
            .orElseThrow(() -> new BookingException(BookingErrorCode.WRONG_ROOM_ID));

        int roomMaxPeople = Integer.parseInt(roomEntity.getMaxPeople());
        int roomMinPeople = Integer.parseInt(roomEntity.getMaxPeople());

        if(roomMinPeople > request.numPeople() && roomMaxPeople < request.numPeople()) {
            throw new BookingException(BookingErrorCode.WRONG_OPTIONS);
        }

        LocalDateTime checkInDatetime = LocalDateTime.of(request.checkInDate(),roomEntity.getAccommodation().getCheckIn());
        LocalDateTime checkOutDatetime = LocalDateTime.of(request.checkOutDate(),roomEntity.getAccommodation().getCheckOut());

        var conflictBookings = bookingRepository.checkConflictingBookings(
            roomEntity.getId(),
            checkInDatetime,
            checkOutDatetime
        );

        if(!conflictBookings.isEmpty()) {
            throw new BookingException(BookingErrorCode.CONFLICT_BOOKING);
        }


        // temporary for test
        var testUser = userRepository.findById(1L);

        // TODO : change user after Spring Security setup
        var bookingEntity = Booking.builder()
            .user(testUser.get())
            .orderId(UUID.randomUUID().toString())
            .room(roomEntity)
            .checkInDatetime(checkInDatetime)
            .checkOutDatetime(checkOutDatetime)
            .build();

        bookingEntity = bookingRepository.save(bookingEntity);


        // TODO : to send a email of booking confirmation


        var response = CreateBookingResponse.builder()
            .orderId(bookingEntity.getOrderId())
            .roomTitle(roomEntity.getTitle())
            .checkInDate(request.checkInDate())
            .checkOutDate(request.checkOutDate())
            .build()
            ;

        return ResponseEntity.ok().body(response);
    }

    @Transactional(readOnly = true)
    public ResponseEntity confirmBooking(int page, int size) {

        // temporary for test
        var testUser = userRepository.findById(1L).get().getId();

        var bookingList = bookingRepository.findAllByUserId(testUser);

        var bookingResponse = ConfirmBookingResponse.builder()
            .bookingList(bookingList.stream()
                .map(booking -> BookingResponse.builder()
                    .orderId(booking.getOrderId())
                    .accommodationTitle(booking.getRoom().getAccommodation().getTitle())
                    .roomTitle(booking.getRoom().getTitle())
                    .roomImg("") // TODO : 확인 필요
                    .minPeople(Integer.parseInt(booking.getRoom().getMinPeople()))
                    .maxPeople(Integer.parseInt(booking.getRoom().getMaxPeople()))
                    .checkInDatetime(booking.getCheckInDatetime())
                    .checkOutDatetime(booking.getCheckOutDatetime())
                    .build())
                .collect(Collectors.toList())
            )
            .build();

        // TODO : pagination

        return ResponseEntity.ok().body(bookingResponse);
    }

}
