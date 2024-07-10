package com.accommodation.accommodation.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.accommodation.accommodation.domain.accommodation.model.entity.Accommodation;
import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.auth.model.entity.User;
import com.accommodation.accommodation.domain.booking.exception.BookingException;
import com.accommodation.accommodation.domain.booking.repository.BookingRepository;
import com.accommodation.accommodation.domain.cart.exception.CartException;
import com.accommodation.accommodation.domain.cart.model.entity.Cart;
import com.accommodation.accommodation.domain.cart.model.request.CartRequest;
import com.accommodation.accommodation.domain.cart.model.response.CartCountResponse;
import com.accommodation.accommodation.domain.cart.model.response.CartListResponse;
import com.accommodation.accommodation.domain.cart.repository.CartRepository;
import com.accommodation.accommodation.domain.cart.service.CartService;
import com.accommodation.accommodation.domain.room.exception.RoomException;
import com.accommodation.accommodation.domain.room.model.entity.Room;
import com.accommodation.accommodation.domain.room.repository.RoomRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomUserDetails customUserDetails;

    @InjectMocks
    private CartService cartService;

    private CartRequest cartRequest;
    private Room room;
    private Cart cart;


    @BeforeEach
    void setUp() {
        Long roomId = 1L;
        Long accommodationId = 1L;
        Long roomPrice = 10000L;
        LocalDate checkInDate = LocalDate.now().plusDays(1);
        LocalDate checkOutDate = LocalDate.now().plusDays(2);
        LocalTime checkInTime = LocalTime.now();
        LocalTime checkOutTime = LocalTime.now().plusHours(5);
        LocalDateTime checkInDateTime = LocalDateTime.of(checkInDate, checkInTime);
        LocalDateTime checkOutDateTime = LocalDateTime.of(checkOutDate,checkOutTime);
        Long totalPrice = ChronoUnit.DAYS.between(checkInDate, checkOutDate) * roomPrice;

        room = Room.builder()
            .id(roomId)
            .price(roomPrice)
            .title("test방")
            .minPeople(2)
            .maxPeople(3)
            .images(List.of("test이미지","test이미지"))
            .accommodation(Accommodation.builder()
                .id(accommodationId)
                .title("test숙소")
                .checkIn(checkInTime)
                .checkOut(checkOutTime)
                .build())
            .build();

        cartRequest = new CartRequest(roomId,2,checkInDate,checkOutDate);

        Long cartId = 1L;

        cart = Cart.builder()
            .id(cartId)
            .checkInDateTime(checkInDateTime)
            .checkOutDateTime(checkOutDateTime)
            .totalPrice(totalPrice)
            .people(2)
            .user(User.builder().id(customUserDetails.getUserId()).build())
            .room(room)
            .build();
    }

    @Test
    @DisplayName("장바구니 추가")
    void createCartSuccess(){
        when(roomRepository.findRoomAndAccommodationById(cartRequest.roomId())).thenReturn(Optional.of(room));

        when(cartRepository.existsByRoomIdAndUserIdAndCheckInDateTimeAndCheckOutDateTime(any(), any(), any(), any()))
            .thenReturn(false);
        when(bookingRepository.checkConflictingBookings(any(), any(), any())).thenReturn(0L);

        cartService.createCart(cartRequest,customUserDetails.getUserId());

        verify(cartRepository, times(1)).save(any(Cart.class));

    }

    @Test
    @DisplayName("올바르지않은 객실의 장바구니 추가")
    void createCart_WrongRoomId() {
        when(roomRepository.findRoomAndAccommodationById(cartRequest.roomId())).thenReturn(Optional.empty());

        assertThrows(
            RoomException.class, () -> cartService.createCart(cartRequest, customUserDetails.getUserId()));

        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("이미 있는 장바구니 추가")
    void createCart_EqualsCartInDB() {
        when(roomRepository.findRoomAndAccommodationById(cartRequest.roomId())).thenReturn(Optional.of(room));

        when(cartRepository.existsByRoomIdAndUserIdAndCheckInDateTimeAndCheckOutDateTime(any(), any(), any(), any()))
            .thenReturn(true);

        assertThrows(CartException.class,()-> cartService.createCart(cartRequest, customUserDetails.getUserId()));

        verify(cartRepository, never()).save(any(Cart.class));

    }

    @Test
    @DisplayName("이미 예약된 장바구니인 경우")
    void createCart_ConflictingBooking(){
        when(roomRepository.findRoomAndAccommodationById(cartRequest.roomId())).thenReturn(Optional.of(room));
        when(cartRepository.existsByRoomIdAndUserIdAndCheckInDateTimeAndCheckOutDateTime(any(), any(), any(), any())).thenReturn(false);
        when(bookingRepository.checkConflictingBookings(any(), any(), any())).thenReturn(1L);

        assertThrows(
            BookingException.class,()-> cartService.createCart(cartRequest, customUserDetails.getUserId()));

        verify(cartRepository, never()).save(any(Cart.class));
    }



    @Test
    @DisplayName("장바구니 조회")
    void findCartSuccess(){
        when(cartRepository.findByUserId(customUserDetails.getUserId())).thenReturn(List.of(cart));
        when(bookingRepository.checkConflictingBookings(any(), any(), any())).thenReturn(0L);

        CartListResponse cartListResponse = cartService.findCartListByUserId(customUserDetails);

        assertThat(cartListResponse.getCartList().size()).isEqualTo(1);
        assertThat(cartListResponse.getCartList().get(0).getCartId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("체크인 날짜가 현재날짜을 지난경우의 장바구니 조회")
    void findCart_ExpiredCart(){
        cart = Cart.builder()
            .id(1L)
            .checkInDateTime(LocalDateTime.now().minusDays(1))
            .checkOutDateTime(LocalDateTime.now().plusDays(2))
            .totalPrice(100000L)
            .people(2)
            .user(User.builder().id(customUserDetails.getUserId()).build())
            .room(room)
            .build();
        when(cartRepository.findByUserId(customUserDetails.getUserId())).thenReturn(List.of(cart));
        when(bookingRepository.checkConflictingBookings(any(), any(), any())).thenReturn(0L);

        CartListResponse cartListResponse = cartService.findCartListByUserId(customUserDetails);

        assertThat(cartListResponse.getCartList().size()).isEqualTo(1);
        assertThat(cartListResponse.getCartList().get(0).getIsBooking()).isEqualTo(false);
    }

    @Test
    @DisplayName("이미 예약이 된 객실 장바구니 조회")
    void findCartListByUserId_ConflictingBooking() {
        when(cartRepository.findByUserId(customUserDetails.getUserId())).thenReturn(List.of(cart));
        when(bookingRepository.checkConflictingBookings(any(), any(), any())).thenReturn(1L);

        CartListResponse cartListResponse = cartService.findCartListByUserId(customUserDetails);

        assertThat(cartListResponse.getCartList().size()).isEqualTo(1);
        assertThat(cartListResponse.getCartList().get(0).getIsBooking()).isEqualTo(false);
    }


    @Test
    @DisplayName("유저의 장바구니 개수 조회")
    void getCartCount(){
        when(cartRepository.countByUserId(customUserDetails.getUserId())).thenReturn(1);

        CartCountResponse cartCount = cartService.getCartCount(customUserDetails);

        assertThat(cartCount.getCartCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("장바구니 삭제")
    void deleteCart(){
        List<Long> cartIdList = List.of(1L,2L);

        when(cartRepository.deleteAllByIds(cartIdList,customUserDetails.getUserId())).thenReturn(2);

        cartService.deleteCartByCartIdList(cartIdList,customUserDetails);

        verify(cartRepository, times(1)).deleteAllByIds(cartIdList, customUserDetails.getUserId());
    }

    @Test
    @DisplayName("올바르지 않은 장바구니 삭제")
    void deleteCart_Fail() {
        List<Long> cartIdList = List.of(1L,2L);

        when(cartRepository.deleteAllByIds(cartIdList,customUserDetails.getUserId())).thenReturn(0);

        assertThrows(CartException.class,()-> cartService.deleteCartByCartIdList(cartIdList,customUserDetails));

    }
}
