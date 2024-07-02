package com.accommodation.accommodation.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    public ResponseCookie createAccessTokenCookie(String token, long duration) {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, token)
            .maxAge(duration)
            .secure(true)
            .httpOnly(true)
            .path("/")
            .sameSite("Lax")
            .build();
    }

    public ResponseCookie createRefreshTokenCookie(String token, long duration) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, token)
            .maxAge(duration)
            .secure(true)
            .httpOnly(true)
            .path("/")
            .sameSite("Lax")
            .build();
    }

    public Optional<String> getAccessTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
            .filter(cookie -> ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue);
    }

    public Optional<String> getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
            .filter(cookie -> REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue);
    }

}
