package com.accommodation.accommodation.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;


    public ResponseEntity<Boolean> logout(String refreshToken) {
        tokenService.dropTokens(refreshToken);

        SecurityContextHolder.clearContext();

// FE 요청으로 임시 주석 처리
//        String cookieString = "accessToken=; Path=/; Max-Age=0; HttpOnly; Secure; SameSite=None"
//            + ", refreshToken=; Path=/; Max-Age=0; HttpOnly; Secure; SameSite=None";

        return ResponseEntity.ok()
//            .header("Set-Cookie", cookieString)
            .body(true);
    }

}
