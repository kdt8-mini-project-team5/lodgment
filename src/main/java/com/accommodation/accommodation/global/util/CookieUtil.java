package com.accommodation.accommodation.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public static final String FRONT_DOMAIN = "fe-mini-project-ten.vercel.app";

    public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    public Cookie createAccessTokenCookie(String token, long duration) {
        Cookie cookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) duration);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "None");
        //cookie.setDomain(FRONT_DOMAIN);

        return cookie;
    }

    public Cookie createRefreshTokenCookie(String token, long duration) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) duration);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "None");
        //cookie.setDomain(FRONT_DOMAIN);

        return cookie;
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

    public void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "None");
        //cookie.setDomain(FRONT_DOMAIN);

        response.addCookie(cookie);
    }

}
