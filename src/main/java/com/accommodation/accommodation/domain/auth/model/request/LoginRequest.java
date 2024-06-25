package com.accommodation.accommodation.domain.auth.model.request;

public record LoginRequest(
        String email,
        String password
) {
}
