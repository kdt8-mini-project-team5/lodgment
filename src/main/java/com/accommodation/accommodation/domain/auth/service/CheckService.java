package com.accommodation.accommodation.domain.auth.service;

import com.accommodation.accommodation.domain.auth.exception.AuthException;
import com.accommodation.accommodation.domain.auth.exception.errorcode.AuthErrorCode;
import com.accommodation.accommodation.domain.auth.model.entity.User;
import com.accommodation.accommodation.domain.auth.repository.UserRepository;
import com.accommodation.accommodation.global.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckService {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtUtil;
    private final HttpServletRequest request;

    @Transactional(readOnly = true)
    public ResponseEntity<Void> check() {
        String token = jwtUtil.getTokenFromRequest(request);

        if (token == null || !token.startsWith("Bearer ")) {
            throw new AuthException(AuthErrorCode.NOT_LOGIN_USER);
        }

        token = token.substring(7).replace(" ", "");

        if (!jwtUtil.validateToken(token, null)) {
            throw new AuthException(AuthErrorCode.NOT_LOGIN_USER);
        }

        Claims claims = jwtUtil.getUserInfoFromToken(token);
        String email = claims.getSubject();

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new AuthException(AuthErrorCode.NOT_LOGIN_USER);
        }

        return ResponseEntity.ok().build();
    }
}