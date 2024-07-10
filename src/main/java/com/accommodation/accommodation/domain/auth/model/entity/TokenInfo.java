package com.accommodation.accommodation.domain.auth.model.entity;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@Getter
@RedisHash("TokenInfo")
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo {

    @Id
    private String refreshToken;
    private String accessToken;

    private String userId;

    private Instant accessTokenExpiresAt;
    private Instant refreshTokenExpiresAt;

    @TimeToLive
    private Long expiration;

    public boolean isAccessTokenExpired() {
        return Instant.now().isAfter(this.accessTokenExpiresAt);
    }

    public boolean isRefreshTokenExpired() {
        return Instant.now().isAfter(this.refreshTokenExpiresAt);
    }

}
