package com.accommodation.accommodation.domain.cart.controller;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.cart.model.request.CartDeleteRequest;
import com.accommodation.accommodation.domain.cart.model.request.CartListRequest;
import com.accommodation.accommodation.domain.cart.model.request.CartRequest;
import com.accommodation.accommodation.domain.cart.model.response.CartCountResponse;
import com.accommodation.accommodation.domain.cart.model.response.CartListResponse;
import com.accommodation.accommodation.domain.cart.service.CartService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("")
    public ResponseEntity createCart(
        @RequestBody @Valid CartRequest cartRequest,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        cartService.createCart(cartRequest, customUserDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    // 페이지 네이션    반환 + totalPage, 몇번째 페이지인지  sort -> 체크인 날짜가 빠른 순
    @GetMapping("")
    public ResponseEntity findCartListByUserId(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){

        CartListResponse response = cartService.findCartListByUserId(customUserDetails);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("")
    public ResponseEntity deleteCartByCartIdList(
        @ModelAttribute @Valid CartDeleteRequest cartDeleteRequest,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        cartService.deleteCartByCartIdList(cartDeleteRequest.cartList(),customUserDetails);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/count")
    public ResponseEntity getCartCount(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        CartCountResponse cartCountResponse = cartService.getCartCount(customUserDetails);
        return ResponseEntity.ok(cartCountResponse);

    }
}
