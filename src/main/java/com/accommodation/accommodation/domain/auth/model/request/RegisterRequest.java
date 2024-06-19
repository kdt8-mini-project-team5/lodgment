package com.accommodation.accommodation.domain.auth.model.request;

public record RegisterRequest(
        String email,
        String password,
        String accessKey,
        String name

) {
}
