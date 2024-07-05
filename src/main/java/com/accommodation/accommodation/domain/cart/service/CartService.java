package com.accommodation.accommodation.domain.cart.service;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.auth.model.entity.User;
import com.accommodation.accommodation.domain.booking.exception.BookingException;
import com.accommodation.accommodation.domain.booking.exception.errorcode.BookingErrorCode;
import com.accommodation.accommodation.domain.booking.model.dto.CartToBookingDTO;
import com.accommodation.accommodation.domain.booking.repository.BookingRepository;
import com.accommodation.accommodation.domain.cart.exception.CartException;
import com.accommodation.accommodation.domain.cart.exception.errorcode.CartErrorCode;
import com.accommodation.accommodation.domain.cart.model.entity.Cart;
import com.accommodation.accommodation.domain.cart.model.request.CartRequest;
import com.accommodation.accommodation.domain.cart.model.response.CartCountResponse;
import com.accommodation.accommodation.domain.cart.model.response.CartListResponse;
import com.accommodation.accommodation.domain.cart.model.response.CartListResponse.CartResponse;
import com.accommodation.accommodation.domain.cart.repository.CartRepository;
import com.accommodation.accommodation.domain.room.model.entity.Room;
import com.accommodation.accommodation.domain.room.repository.RoomRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final RoomRepository roomRepository;

    private final CartRepository cartRepository;

    private final BookingRepository bookingRepository;

    public void createCart(CartRequest cartRequest, Long userId) {
        // room 이 올바른지 확인 후 price 가져오기
        Room room = roomRepository.findRoomAndAccommodationById(cartRequest.roomId())
            .orElseThrow(() -> new RoomException(RoomErrorCode.ROOM_NOT_FOUND));

        LocalDateTime checkInDatetime = LocalDateTime.of(cartRequest.checkInDate(),
            room.getAccommodation().getCheckIn());
        LocalDateTime checkOutDatetime = LocalDateTime.of(cartRequest.checkOutDate(),
            room.getAccommodation().getCheckOut());
        Long totalPrice =
            ChronoUnit.DAYS.between(checkInDatetime.toLocalDate(), checkOutDatetime.toLocalDate())
                * room.getPrice();

        // 같은 요청의 장바구니가 이미 있는지 확인
        checkEqualsCartInDB(cartRequest.roomId(),userId,checkInDatetime,checkOutDatetime);

        // 예약 가능한 장바구니 인지
        if(isConflictingBookings(cartRequest.roomId(),checkInDatetime,checkOutDatetime)){
            throw new BookingException(BookingErrorCode.CONFLICT_BOOKING);
        }

        // 장바구니에 저장
        Cart cart = Cart.builder()
            .user(User.builder().id(userId).build())
            .room(room)
            .totalPrice(totalPrice)
            .people(cartRequest.people())
            .checkInDateTime(checkInDatetime)
            .checkOutDateTime(checkOutDatetime)
            .build();

        cartRepository.save(cart);
    }

    private void checkEqualsCartInDB(Long roomId, Long userId, LocalDateTime checkInDatetime, LocalDateTime checkOutDatetime){
        if (cartRepository.existsByRoomIdAndUserIdAndCheckInDateTimeAndCheckOutDateTime(roomId,userId,checkInDatetime,checkOutDatetime)){
            throw new CartException(CartErrorCode.EQUALS_CART_IN_DB);
        }
    }

    private boolean isConflictingBookings(Long roomId, LocalDateTime checkInDateTime, LocalDateTime checkOutDateTime){
        long conflictBookingCount = bookingRepository.checkConflictingBookings(
            roomId,
            checkInDateTime,
            checkOutDateTime
        );

        if (conflictBookingCount > 0) {
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public CartListResponse findCartListByUserId(CustomUserDetails customUserDetails) {
        //cart 정보 가져오기
        List<Cart> cartList = cartRepository.findByUserId(customUserDetails.getUserId());

        return CartListResponse.builder()
            .cartList(cartList.stream()
                .map(cart -> {
                    // 가져온 장바구니의 예약 못하는 값들 확인
                    Boolean isBooking = true;
                    // 체크아웃 날짜가 현재날짜보다 이후인 경우 false
                    if(cart.getCheckInDateTime().toLocalDate().isBefore(LocalDate.now())){
                        isBooking = false;
                    }
                    // 예약 내역 확인 후 예약 못하는 경우 false
                    if(isConflictingBookings(cart.getRoom().getId(), cart.getCheckInDateTime(),cart.getCheckOutDateTime())){
                        isBooking = false;
                    }
                    return CartResponse.builder()
                    .cartId(cart.getId())
                    .checkInDatetime(cart.getCheckInDateTime())
                    .checkOutDatetime(cart.getCheckOutDateTime())
                    .totalPrice(cart.getTotalPrice())
                    .people(cart.getPeople())
                    .roomId(cart.getRoom().getId())
                    .roomTitle(cart.getRoom().getTitle())
                    .minPeople(cart.getRoom().getMinPeople())
                    .maxPeople(cart.getRoom().getMaxPeople())
                    .roomImg(cart.getRoom().getImages().get(1))
                    .accommodationId(cart.getRoom().getAccommodation().getId())
                    .accommodationTitle(cart.getRoom().getAccommodation().getTitle())
                        .isBooking(isBooking)
                    .build();
                })
                .toList()
            )
            .build();
    }

    @Transactional(readOnly = true)
    public CartCountResponse getCartCount(CustomUserDetails customUserDetails) {
        return CartCountResponse.builder().cartCount(cartRepository.countByUserId(customUserDetails.getUserId())).build();
    }

    public void deleteCartByCartIdList(List<Long> cartIdList, CustomUserDetails customUserDetails) {

        int deleteCount = cartRepository.deleteAllByIds(cartIdList, customUserDetails.getUserId());

        if (deleteCount == 0) {
            throw new CartException(CartErrorCode.FAIL_DELETE);
        }
    }


    @Transactional
    public Optional<CartToBookingDTO> getCartForBooking(Long cartId) {
        var result =  cartRepository.findById(cartId)
            .map(cart -> CartToBookingDTO.builder()
                .userId(cart.getUser().getId())
                .roomId(cart.getRoom().getId())
                .people(cart.getPeople())
                .accommodationTitle(cart.getRoom().getAccommodation().getTitle())
                .roomTitle(cart.getRoom().getTitle())
                .totalPrice(cart.getTotalPrice())
                .checkInDatetime(cart.getCheckInDateTime())
                .checkOutDatetime(cart.getCheckOutDateTime())
                .maxPeople(cart.getRoom().getMaxPeople())
                .minPeople(cart.getRoom().getMinPeople())
                .build());

        deleteCartById(cartId);

        return result;
    }

    public void deleteCartById(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}
