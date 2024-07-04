package com.accommodation.accommodation.domain.auth.config.filter;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.auth.exception.AuthException;
import com.accommodation.accommodation.domain.auth.exception.errorcode.AuthErrorCode;
import com.accommodation.accommodation.domain.auth.model.entity.TokenInfo;
import com.accommodation.accommodation.domain.auth.model.request.LoginRequest;
import com.accommodation.accommodation.domain.auth.service.TokenService;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import org.springframework.stereotype.Component;

@Slf4j(topic = "로그인")
@Component
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenService tokenService;

    public LoginFilter(AuthenticationManager authenticationManager, TokenService tokenService) {
        super(authenticationManager);
        this.tokenService = tokenService;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(),
                    LoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error("올바르지 않은 로그인 시도");
            throw new AuthException(AuthErrorCode.LOGIN_FAILED);
        }
    }


    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException {
        log.info("로그인 성공");
        String userId = ((CustomUserDetails) authResult.getPrincipal()).getUsername();

        TokenInfo tokenInfo = tokenService.createTokens(userId);

        Cookie accessTokenCookie = tokenService.createAccessTokenCookie(
                tokenInfo.getAccessToken());
        Cookie refreshTokenCookie = tokenService.createRefreshTokenCookie(
                tokenInfo.getRefreshToken());

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        response.setStatus(HttpServletResponse.SC_OK);
    }


    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException {

        log.info("로그인 실패");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}