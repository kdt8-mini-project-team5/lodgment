package com.accommodation.accommodation.domain.booking.service;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.booking.exception.BookingException;
import com.accommodation.accommodation.domain.booking.exception.errorcode.BookingErrorCode;
import com.accommodation.accommodation.domain.booking.model.dto.BookingDTO;
import com.accommodation.accommodation.domain.booking.model.request.CreateBookingRequest;
import com.accommodation.accommodation.domain.booking.model.response.ConfirmBookingsResponse;
import com.accommodation.accommodation.domain.booking.model.response.ConfirmBookingsResponse.BookingResponse;
import com.accommodation.accommodation.domain.booking.model.response.CreateBookingsResponse;
import com.accommodation.accommodation.domain.booking.repository.BookingRepository;
import com.accommodation.accommodation.domain.room.repository.RoomRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
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
            .orElseThrow(() -> new BookingException(BookingErrorCode.ROOM_NOT_FOUND));

        int roomMaxPeople = roomDetails.getMaxPeople();
        int roomMinPeople = roomDetails.getMinPeople();
        if (roomMinPeople > request.numPeople() || roomMaxPeople < request.numPeople()) {
            throw new BookingException(BookingErrorCode.WRONG_OPTIONS);
        }

        LocalDateTime checkInDatetime = LocalDateTime.of(request.checkInDate(),
            roomDetails.getCheckInTime());
        LocalDateTime checkOutDatetime = LocalDateTime.of(request.checkOutDate(),
            roomDetails.getCheckOutTime());

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
            .guestName(request.guestName())
            .guestTel(request.guestTel())
            .roomId(request.roomId())
            .checkInDatetime(checkInDatetime)
            .checkOutDatetime(checkOutDatetime)
            .people(request.numPeople())
            .totalPrice(totalPrice)
            .build();

        var bookingResult = bookingRepository.saveBooking(booking);
        // TODO : DB에서 발생한 예약 실패에 대한 예외 작업

        // TODO : to send a email of booking confirmation

        String roomImage = roomRepository.findRoomImageById(request.roomId())
            .flatMap(images -> images.stream().findFirst())
            .orElse("");

        var response = CreateBookingsResponse.builder()
            .items(Collections.singletonList(
                CreateBookingsResponse.BookingResult.builder()
                    .orderId(booking.getOrderId())
                    .guestName(request.guestName())
                    .guestTel(request.guestTel())
                    .accommodationTitle(roomDetails.getAccommodationTitle())
                    .roomTitle(roomDetails.getRoomTitle())
                    .roomImage(roomImage)
                    .minPeople(roomMinPeople)
                    .maxPeople(roomMaxPeople)
                    .checkInDatetime(checkInDatetime)
                    .checkOutDatetime(checkOutDatetime)
                    .totalPrice(totalPrice)
                    .build()
            ))
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
        var bookingList = bookingRepository.findAllByUserId(customUserDetails.getUserId(),
            pageable);

        var bookingResponse = ConfirmBookingsResponse.builder()
            .bookingList(bookingList.stream()
                .map(booking -> {
                    var roomImages = booking.getRoom().getImages();
                    var firstImg = roomImages != null && !roomImages.isEmpty()
                        ? roomImages.get(0)
                        : "";

                    return BookingResponse.builder()
                        .orderId(booking.getOrderId())
                        .guestName(booking.getGuestName())
                        .guestTel(booking.getGuestTel())
                        .accommodationTitle(booking.getRoom().getAccommodation().getTitle())
                        .roomTitle(booking.getRoom().getTitle())
                        .roomImg(firstImg)
                        .minPeople(booking.getRoom().getMinPeople())
                        .maxPeople(booking.getRoom().getMaxPeople())
                        .checkInDatetime(booking.getCheckInDatetime())
                        .checkOutDatetime(booking.getCheckOutDatetime())
                        .totalPrice(booking.getTotalPrice())
                        .build();
                })
                .collect(Collectors.toList())
            )
            .totalElements(bookingList.getTotalElements())
            .build();

        return ResponseEntity.ok().body(bookingResponse);
    }

}
