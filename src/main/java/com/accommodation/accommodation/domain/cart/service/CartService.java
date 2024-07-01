package com.accommodation.accommodation.domain.cart.service;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.auth.model.entity.User;
import com.accommodation.accommodation.domain.booking.exception.BookingException;
import com.accommodation.accommodation.domain.booking.exception.errorcode.BookingErrorCode;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final RoomRepository roomRepository;

    private final CartRepository cartRepository;

    public void createCart(CartRequest cartRequest, Long userId) {
        // room 이 올바른지 확인 후 price 가져오기
        Room room = roomRepository.findRoomAndAccommodationById(cartRequest.roomId())
            .orElseThrow(() -> new BookingException(BookingErrorCode.WRONG_ROOM_ID));

        LocalDateTime checkInDatetime = LocalDateTime.of(cartRequest.checkInDate(),
            room.getAccommodation().getCheckIn());
        LocalDateTime checkOutDatetime = LocalDateTime.of(cartRequest.checkOutDate(),
            room.getAccommodation().getCheckOut());
        Long totalPrice =
            ChronoUnit.DAYS.between(checkInDatetime.toLocalDate(), checkOutDatetime.toLocalDate())
                * room.getPrice();
        // 같은 요청의 장바구니가 이미 있는지 확인
        checkEqualsCartInDB(cartRequest.roomId(),userId,checkInDatetime,checkOutDatetime);

        // 장바구니에 저장
        Cart cart = Cart.builder()
            .user(User.builder().id(userId).build())
            .room(room)
            .totalPrice(totalPrice)
            .people(cartRequest.people())
            .checkInDateTime(checkInDatetime)
            .checkOutDateTime(checkOutDatetime)
            .accommodationId(room.getAccommodation().getId())
            .accommodationTitle(room.getAccommodation().getTitle())
            .build();

        cartRepository.save(cart);
    }

    private void checkEqualsCartInDB(Long roomId, Long userId, LocalDateTime checkInDatetime, LocalDateTime checkOutDatetime){
        if (cartRepository.existsByRoomIdAndUserIdAndCheckInDateTimeAndCheckOutDateTime(roomId,userId,checkInDatetime,checkOutDatetime)){
            throw new CartException(CartErrorCode.EQUALS_CART_IN_DB);
        }
    }


    public CartListResponse findCartListByUserId(CustomUserDetails customUserDetails,
        Pageable pageable) {
        //cart 정보 가져오기
        Page<Cart> cartPage = cartRepository.findByUserId(customUserDetails.getUserId(), pageable);

        return CartListResponse.builder()
            .cartList(cartPage.stream()
                .map(cart -> CartResponse.builder()
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
                    .accommodationId(cart.getAccommodationId())
                    .accommodationTitle(cart.getAccommodationTitle())
                    .build())
                .toList()
            )
            .totalPage(cartPage.getTotalPages())
            .build();
    }


    public CartCountResponse getCartCount(CustomUserDetails customUserDetails) {
        return CartCountResponse.builder().cartCount(cartRepository.countByUserId(customUserDetails.getUserId())).build();
    }

    public void deleteCartByCartIdList(List<Long> cartIdList, CustomUserDetails customUserDetails) {

        int deleteCount = cartRepository.deleteAllByIds(cartIdList, customUserDetails.getUserId());

        if (deleteCount == 0) {
            throw new CartException(CartErrorCode.FAIL_DELETE);
        }
    }
}
