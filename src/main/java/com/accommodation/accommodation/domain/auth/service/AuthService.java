package com.accommodation.accommodation.domain.auth.service;

import com.accommodation.accommodation.domain.auth.exception.AuthException;
import com.accommodation.accommodation.domain.auth.exception.errorcode.AuthErrorCode;
import com.accommodation.accommodation.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final CookieUtil cookieUtil;

    public ResponseEntity<Boolean> logout(
        HttpServletRequest request, HttpServletResponse response
    ) {

        String refreshToken = cookieUtil.getRefreshTokenFromCookie(request)
            .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_JWT_TOKEN));

        tokenService.dropTokens(refreshToken);

        SecurityContextHolder.clearContext();

        cookieUtil.deleteCookie(response, CookieUtil.ACCESS_TOKEN_COOKIE_NAME);
        cookieUtil.deleteCookie(response, CookieUtil.REFRESH_TOKEN_COOKIE_NAME);

        return ResponseEntity.ok().body(true);
    }
}
