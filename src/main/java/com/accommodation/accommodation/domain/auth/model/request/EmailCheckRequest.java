package com.accommodation.accommodation.domain.auth.model.request;

import com.accommodation.accommodation.global.validation.type.EmailValidation;

public record EmailCheckRequest(
        @EmailValidation
        String email,
        String accessKey
) {
}
