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

    public ResponseEntity<Boolean> checkLoginStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
        && !(authentication.getPrincipal() instanceof String)) {
            // 로그인 되어 있는 경우
            // CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal(); // 사용자 ID가 필요하다면 사용
            return ResponseEntity.status(HttpStatus.OK).body(true);

        } else {
            // 로그인 되어 있지 않은 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    public ResponseEntity<Boolean> logout() {
        SecurityContextHolder.clearContext();

        String cookieString = "accessToken=; Path=/; Max-Age=0; HttpOnly; Secure; SameSite=None"
            + ", refreshToken=; Path=/; Max-Age=0; HttpOnly; Secure; SameSite=None";

        return ResponseEntity.ok()
            .header("Set-Cookie", cookieString)
            .body(true);
    }

}
