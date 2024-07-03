package com.accommodation.accommodation.domain.auth.config.filter;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.auth.service.TokenService;
import com.accommodation.accommodation.domain.auth.service.UserDetailsServiceImpl;
import com.accommodation.accommodation.global.util.CookieUtil;
import com.accommodation.accommodation.global.util.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 필터")
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;
    private final UserDetailsServiceImpl userDetailsService;

    private static final List<String> EXCLUDE_URL = Collections.singletonList("/api/login");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        cookieUtil.getAccessTokenFromCookie(request).ifPresent(accessToken -> {

            try {
                if(tokenService.validateAccessToken(accessToken)) {
                    // if Valid AccessToken
                    String userId = jwtProvider.getUserIdFromToken(accessToken);
                    setAuthentication(userId);
                } else {
                    // if Invalid AccessToken
                    cookieUtil.getRefreshTokenFromCookie(request).ifPresent(refreshToken -> {
                        tokenService.renewTokens(refreshToken).ifPresent(newTokenInfo -> {

                            ResponseCookie newAccessTokenCookie = tokenService.createAccessTokenCookie(
                                newTokenInfo.getAccessToken());
                            ResponseCookie newRefreshTokenCookie = tokenService.createRefreshTokenCookie(
                                newTokenInfo.getRefreshToken());

                            response.addHeader(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString());
                            response.addHeader(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString());

                            setAuthentication(newTokenInfo.getUserId());
                        });
                    });
                }
            } catch (Exception e) {
                log.error("JWT 인증 실패 ", e);
            }
        });

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String userId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(userId);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String userId) {
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
            userId);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDE_URL.stream()
            .anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }
}
