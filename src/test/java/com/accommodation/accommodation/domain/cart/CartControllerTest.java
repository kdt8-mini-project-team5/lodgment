package com.accommodation.accommodation.domain.cart;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.cart.model.request.CartRequest;
import com.accommodation.accommodation.domain.cart.model.response.CartCountResponse;
import com.accommodation.accommodation.domain.cart.model.response.CartListResponse;
import com.accommodation.accommodation.domain.cart.model.response.CartListResponse.CartResponse;
import com.accommodation.accommodation.domain.cart.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomUserDetails customUserDetails;

    private CartListResponse cartListResponse;

    private CartCountResponse cartCountResponse;

    private CartRequest cartRequest;

    @BeforeEach
    void setUp() {
        Long roomId = 1L;

        customUserDetails = new CustomUserDetails(1L, "testUser@gmail.com", "password");

        LocalDate checkInDate = LocalDate.now().plusDays(1);
        LocalDate checkOutDate = LocalDate.now().plusDays(2);
        cartRequest = new CartRequest(roomId,2,checkInDate,checkOutDate);

        CartResponse cartResponse = CartResponse.builder().cartId(1L).build();
        cartListResponse = CartListResponse.builder().cartList(List.of(cartResponse)).build();

        cartCountResponse = CartCountResponse.builder().cartCount(1).build();
    }

    @Test
    @DisplayName("장바구니 추가")
    void createCartSuccess() throws Exception {
        doNothing().when(cartService).createCart(any(CartRequest.class), any(Long.class));

        mockMvc.perform(post("/api/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(cartRequest))
                .with(user(customUserDetails))
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("장바구니 조회")
    void findCartListByUserId() throws Exception {
        when(cartService.findCartListByUserId(customUserDetails)).thenReturn(cartListResponse);

        mockMvc.perform(get("/api/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(customUserDetails))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.cartList",hasSize(1)))
            .andExpect(jsonPath("$.cartList[0].cartId",is(1)));
    }

    @Test
    @DisplayName("장바구니 삭제")
    void deleteCartByCartIdList() throws Exception {
        doNothing().when(cartService).deleteCartByCartIdList(anyList(), any(CustomUserDetails.class));

        mockMvc.perform(delete("/api/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .param("cartList","1","2")
                .with(user(customUserDetails))
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("장바구니 갯수 조회")
    void getCartCount() throws Exception {
        when(cartService.getCartCount(customUserDetails)).thenReturn(cartCountResponse);

        mockMvc.perform(get("/api/cart/count")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(customUserDetails))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.cartCount",is(1)));
    }



}
