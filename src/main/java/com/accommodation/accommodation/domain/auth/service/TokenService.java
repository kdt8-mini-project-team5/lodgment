package com.accommodation.accommodation.domain.auth.service;

import com.accommodation.accommodation.domain.auth.model.entity.TokenInfo;
import com.accommodation.accommodation.domain.auth.repository.TokenRepository;
import com.accommodation.accommodation.global.util.CookieUtil;
import com.accommodation.accommodation.global.util.JwtProvider;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;
    private final CookieUtil cookieUtil;

    public TokenInfo createTokens(String userId) {
        String accessToken = jwtProvider.createAccessToken(userId);
        String refreshToken = jwtProvider.createRefreshToken(userId);

        TokenInfo tokenInfo = TokenInfo.builder()
            .refreshToken(refreshToken)
            .accessToken(accessToken)
            .userId(userId)
            .accessTokenExpiresAt(Instant.now().plusMillis(jwtProvider.getAccessTokenExpiration()))
            .refreshTokenExpiresAt(
                Instant.now().plusMillis(jwtProvider.getRefreshTokenExpiration()))
            .expiration(jwtProvider.getRefreshTokenExpiration() / 1000)
            .build();

        return tokenRepository.save(tokenInfo);
    }

    public ResponseCookie createAccessTokenCookie(String token) {
        return cookieUtil.createAccessTokenCookie(token, jwtProvider.getRefreshTokenExpiration() / 1000);
    }

    public ResponseCookie createRefreshTokenCookie(String token) {
        return cookieUtil.createRefreshTokenCookie(token,
            jwtProvider.getRefreshTokenExpiration() / 1000);
    }

    public boolean validateAccessToken(String accessToken) {
        return jwtProvider.validateToken(accessToken) && jwtProvider.isAccessToken(accessToken);
            //&& tokenRepository.findByAccessToken(accessToken).isPresent(); // Redis 교차 검증은 추후 결정
    }

    public boolean validateRefreshToken(String refreshToken) {
        return jwtProvider.validateToken(refreshToken) && jwtProvider.isRefreshToken(refreshToken)
            && tokenRepository.findById(refreshToken)
            .map(tokenInfo -> !tokenInfo.isRefreshTokenExpired())
            .orElse(false);
    }

    public Optional<TokenInfo> renewTokens(String refreshToken) {
        return tokenRepository.findById(refreshToken)
            .filter(tokenInfo -> !tokenInfo.isRefreshTokenExpired())
            .map(tokenInfo ->
            {
                String userId = tokenInfo.getUserId();
                tokenRepository.deleteById(refreshToken);
                return createTokens(userId);
            });
    }

    public Optional<TokenInfo> getTokenInfo(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken);
    }

    public void dropTokens(String refreshToken) {
        tokenRepository.deleteById(refreshToken);
    }

}
