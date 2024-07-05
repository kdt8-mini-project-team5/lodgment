package com.accommodation.accommodation.domain.auth.controller;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/check")
    public ResponseEntity checkLoggedIn(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        log.info(customUserDetails.getEmail() + " 접근");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout(
            HttpServletRequest request, HttpServletResponse response
    ) {
        return authService.logout(request, response);
    }

}
