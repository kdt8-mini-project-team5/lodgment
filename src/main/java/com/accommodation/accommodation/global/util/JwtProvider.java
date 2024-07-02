package com.accommodation.accommodation.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j(topic = "Login 필터")
@Component
public class JwtProvider {

    @Value("${jwt.key}")
    private String jwtSecret;

    // 테스트를 위해 짧게 수정 + 추후 application 수정 필요
    //@Value("${jwt.access-token.expiration}")
    //private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분
    //@Value("${jwt.refresh-token.expiration}")
    //private final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일

    private final long ACCESS_TOKEN_TIME = 1 * 10 * 1000L; // 1분
    private final long REFRESH_TOKEN_TIME = 1 * 30 * 1000L; // 2분

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init(){
        byte[] bytes = Base64.getDecoder().decode(jwtSecret);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAccessToken(String username) {
        return createToken(username, ACCESS_TOKEN_TIME, "ACCESS");
    }

    public String createRefreshToken(String username) {
        return createToken(username, REFRESH_TOKEN_TIME, "REFRESH");
    }


    private String createToken(String userId, long expiration, String tokenType) {
        Date now = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + expiration);

        return Jwts.builder()
            .setSubject(userId)
            .setExpiration(expireDate)
            .setIssuedAt(now)
            .claim("tokenType", tokenType)
            .signWith(key, signatureAlgorithm)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    public boolean isAccessToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        return "ACCESS".equals(claims.get("tokenType"));
    }

    public boolean isRefreshToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        return "REFRESH".equals(claims.get("tokenType"));
    }

    public long getAccessTokenExpiration() {
        return ACCESS_TOKEN_TIME;
    }

    public long getRefreshTokenExpiration() {
        return REFRESH_TOKEN_TIME;
    }


}
