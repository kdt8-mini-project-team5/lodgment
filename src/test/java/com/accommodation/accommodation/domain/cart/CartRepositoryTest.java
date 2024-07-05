package com.accommodation.accommodation.domain.cart;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.accommodation.accommodation.domain.accommodation.model.entity.Accommodation;
import com.accommodation.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.accommodation.accommodation.domain.auth.model.entity.User;
import com.accommodation.accommodation.domain.auth.repository.UserRepository;
import com.accommodation.accommodation.domain.cart.model.entity.Cart;
import com.accommodation.accommodation.domain.cart.repository.CartRepository;
import com.accommodation.accommodation.domain.room.model.entity.Room;
import com.accommodation.accommodation.domain.room.repository.RoomRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CartRepositoryTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccommodationRepository accommodationRepository;

    @Autowired
    RoomRepository roomRepository;

    private User saveUser;

    private Accommodation saveAccommodation;

    private Room saveRoom;

    @BeforeEach
    void setUp() {
        saveUser = userRepository.save(createUser());
        saveAccommodation = accommodationRepository.save(createAccommodation(1L));
        saveRoom = roomRepository.save(createRoom(1L,saveAccommodation));
    }

    @Test
    @DisplayName("Cart 추가")
    void createCart(){
        Cart cart = createCart(saveUser, saveRoom, 3, 50000L, LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2));

        Cart resultCart = cartRepository.save(cart);

        assertThat(resultCart.getId()).isNotNull();
    }

    @Test
    @DisplayName("User의 장바구니 조회")
    void findByUserId(){
        Cart cart1 = createCart(saveUser, saveRoom, 3, 50000L, LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2));
        Cart cart2 = createCart(saveUser, saveRoom, 3, 50000L, LocalDateTime.now().plusDays(3),
            LocalDateTime.now().plusDays(4));
        cartRepository.save(cart1);
        cartRepository.save(cart2);

        List<Cart> cartList = cartRepository.findByUserId(saveUser.getId());

        assertThat(cartList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("User의 장바구니 개수 조회")
    void countByUserId(){
        Cart cart1 = createCart(saveUser, saveRoom, 3, 50000L, LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2));
        Cart cart2 = createCart(saveUser, saveRoom, 3, 50000L, LocalDateTime.now().plusDays(3),
            LocalDateTime.now().plusDays(4));
        cartRepository.save(cart1);
        cartRepository.save(cart2);

        Integer countByUserId = cartRepository.countByUserId(saveUser.getId());

        assertThat(countByUserId).isEqualTo(2);
    }

    @Test
    @DisplayName("장바구니 삭제")
    void deleteAllByIds(){
        Cart cart1 = createCart(saveUser, saveRoom, 3, 50000L, LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2));
        Cart cart2 = createCart(saveUser, saveRoom, 3, 50000L, LocalDateTime.now().plusDays(3),
            LocalDateTime.now().plusDays(4));
        cartRepository.save(cart1);
        cartRepository.save(cart2);
        List<Long> ids = new ArrayList<>();
        ids.add(cart1.getId());
        ids.add(cart2.getId());

        int i = cartRepository.deleteAllByIds(ids, saveUser.getId());

        assertThat(i).isEqualTo(2);
    }

    @Test
    @DisplayName("같은 내요의 장바구니가 있는지 확인")
    void checkConflictingCart(){
        LocalDateTime checkInDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime checkOutDateTime = checkInDateTime.plusDays(1);
        Cart cart = createCart(saveUser, saveRoom, 3, 50000L, checkInDateTime, checkOutDateTime);
        cartRepository.save(cart);

        Boolean isConflict = cartRepository.existsByRoomIdAndUserIdAndCheckInDateTimeAndCheckOutDateTime(
            saveRoom.getId(), saveUser.getId(), checkInDateTime, checkOutDateTime);

        assertThat(isConflict).isTrue();
    }

    private Cart createCart(User user,Room room,int numPeople,Long totalPrice, LocalDateTime checkInDateTime,LocalDateTime checkOutDateTime){
        return Cart.builder()
            .user(user)
            .room(room)
            .people(numPeople)
            .totalPrice(totalPrice)
            .checkInDateTime(checkInDateTime)
            .checkOutDateTime(checkOutDateTime)
            .build();
    }

    private User createUser(){
        return User.builder()
            .email("test@gmail.com")
            .password("testPassword")
            .name("홍길동")
            .build();
    }

    private Room createRoom(Long testNum,Accommodation accommodation){
        return Room.builder()
            .title("room"+testNum)
            .price(10000*testNum)
            .minPeople(2)
            .maxPeople(3)
            .accommodation(accommodation)
            .build();
    }

    private Accommodation createAccommodation(Long testNUm){
        return Accommodation.builder()
            .title("accommodation"+testNUm)
            .info("테스트 accommodation"+testNUm)
            .minPrice(10000*testNUm)
            .build();
    }
}
