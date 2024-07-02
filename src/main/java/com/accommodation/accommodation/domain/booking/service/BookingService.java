package com.accommodation.accommodation.domain.booking.service;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.booking.exception.BookingException;
import com.accommodation.accommodation.domain.booking.exception.errorcode.BookingErrorCode;
import com.accommodation.accommodation.domain.booking.facade.BookingLockFacade;
import com.accommodation.accommodation.domain.booking.model.dto.BookingDTO;
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

    @Transactional
    public ResponseEntity createBooking(
        CustomUserDetails customUserDetails,
        CreateBookingRequest request
        ) {
        var roomDetails = roomRepository.findRoomDetailsById(request.roomId())
            .orElseThrow(() -> new BookingException(BookingErrorCode.WRONG_ROOM_ID));

        int roomMaxPeople = roomDetails.getMaxPeople();
        int roomMinPeople = roomDetails.getMinPeople();
        if (roomMinPeople > request.numPeople() || roomMaxPeople < request.numPeople()) {
            throw new BookingException(BookingErrorCode.WRONG_OPTIONS);
        }

        LocalDateTime checkInDatetime = LocalDateTime.of(request.checkInDate(),
            roomDetails.getCheckIn());
        LocalDateTime checkOutDatetime = LocalDateTime.of(request.checkOutDate(),
            roomDetails.getCheckOut());


        long conflictBookingCount = bookingRepository.checkConflictingBookings(
            request.roomId(),
            checkInDatetime,
            checkOutDatetime
        );

        if (conflictBookingCount > 0) {
            throw new BookingException(BookingErrorCode.CONFLICT_BOOKING);
        }

        long totalPrice =
            ChronoUnit.DAYS.between(checkInDatetime.toLocalDate(), checkOutDatetime.toLocalDate())
                * roomDetails.getPrice();

        var booking = BookingDTO.builder()
            .userId(customUserDetails.getUserId())
            .orderId(UUID.randomUUID().toString())
            .roomId(request.roomId())
            .checkInDatetime(checkInDatetime)
            .checkOutDatetime(checkOutDatetime)
            .people(request.numPeople())
            .totalPrice(totalPrice)
            .build();

        var bookingResult = bookingRepository.saveBooking(booking);
        // TODO : DB에서 발생한 예약 실패에 대한 예외 작업


        // TODO : to send a email of booking confirmation

        var response = CreateBookingResponse.builder()
            .orderId(booking.getOrderId())
            .roomTitle(roomDetails.getTitle())
            .checkInDate(request.checkInDate())
            .checkOutDate(request.checkOutDate())
            .totalPrice(totalPrice)
            .build();

        return ResponseEntity.ok().body(response);
    }


    @Transactional(readOnly = true)
    public ResponseEntity confirmBooking(
        CustomUserDetails customUserDetails,
        int page,
        int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        var bookingList = bookingRepository.findAllByUserId(customUserDetails.getUserId(), pageable);

        var bookingResponse = ConfirmBookingResponse.builder()
            .bookingList(bookingList.stream()
                .map(booking -> BookingResponse.builder()
                    .orderId(booking.getOrderId())
                    .accommodationTitle(booking.getRoom().getAccommodation().getTitle())
                    .roomTitle(booking.getRoom().getTitle())
                    .roomImg("") // TODO : 확인 필요 (이미지 1개만 전달해 주는 지)
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
