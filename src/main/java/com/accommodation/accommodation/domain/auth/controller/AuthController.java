package com.accommodation.accommodation.domain.auth.controller;

import com.accommodation.accommodation.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/check")
    public ResponseEntity checkLoggedIn() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout(
            @CookieValue(name = "refreshToken") String refreshToken
    ) {
        return authService.logout(refreshToken);
    }

}
