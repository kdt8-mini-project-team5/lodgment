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
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        int roomMaxPeople = roomEntity.getMaxPeople();
        int roomMinPeople = roomEntity.getMinPeople();
        if(roomMinPeople > request.numPeople() || roomMaxPeople < request.numPeople()) {
            System.out.println("room : " + request.numPeople() + " min : " + roomMinPeople + " max : " + roomMaxPeople);
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

        long totalPrice = ChronoUnit.DAYS.between(checkInDatetime.toLocalDate(), checkOutDatetime.toLocalDate()) * roomEntity.getPrice();

        // temporary for test
        var testUser = userRepository.findById(1L);

        // TODO : change user after Spring Security setup
        var bookingEntity = Booking.builder()
            .user(testUser.get())
            .orderId(UUID.randomUUID().toString())
            .room(roomEntity)
            .checkInDatetime(checkInDatetime)
            .checkOutDatetime(checkOutDatetime)
            .people(request.numPeople())
            .totalPrice(totalPrice)
            .build();

        bookingEntity = bookingRepository.save(bookingEntity);


        // TODO : to send a email of booking confirmation

        var response = CreateBookingResponse.builder()
            .orderId(bookingEntity.getOrderId())
            .roomTitle(roomEntity.getTitle())
            .checkInDate(request.checkInDate())
            .checkOutDate(request.checkOutDate())
            .totalPrice(totalPrice)
            .build()
            ;

        return ResponseEntity.ok().body(response);
    }


    @Transactional(readOnly = true)
    public ResponseEntity confirmBooking(int page, int size) {

        // temporary for test
        var userId = userRepository.findById(1L).get().getId();

        Pageable pageable = PageRequest.of(page, size);
        var bookingList = bookingRepository.findAllByUserId(userId, pageable);

        var bookingResponse = ConfirmBookingResponse.builder()
            .bookingList(bookingList.stream()
                .map(booking -> BookingResponse.builder()
                    .orderId(booking.getOrderId())
                    .accommodationTitle(booking.getRoom().getAccommodation().getTitle())
                    .roomTitle(booking.getRoom().getTitle())
                    .roomImg("") // TODO : 확인 필요 (이미지 1개만 전달해 주는 지?)
                    .minPeople(booking.getRoom().getMinPeople())
                    .maxPeople(booking.getRoom().getMaxPeople())
                    .checkInDatetime(booking.getCheckInDatetime())
                    .checkOutDatetime(booking.getCheckOutDatetime())
                    .totalPrice(booking.getTotalPrice())
                    .build())
                .collect(Collectors.toList())
            )
            .totalElements(bookingList.getTotalElements())
            .build();

        return ResponseEntity.ok().body(bookingResponse);
    }

}
