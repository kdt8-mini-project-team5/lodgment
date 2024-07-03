package com.accommodation.accommodation.domain.auth.controller;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkLoggedIn() {
        return authService.checkLoginStatus();
    }

    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout() {
        return authService.logout();
    }

}
