package com.accommodation.accommodation.domain.auth.model.request;

import com.accommodation.accommodation.global.validation.type.EmailValidation;
import com.accommodation.accommodation.global.validation.type.PasswordValidation;

public record RegisterRequest(
        @EmailValidation
        String email,
        @PasswordValidation
        String password,
        String accessKey,
        String name

) {
}
