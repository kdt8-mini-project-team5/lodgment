package com.accommodation.accommodation.domain.auth.model.request;

public record EmailCheckRequest(
        String email,
        String accessKey
) {
}
