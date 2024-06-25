package com.accommodation.accommodation.domain.auth.model.request;

import com.accommodation.accommodation.global.validation.type.EmailValidation;
import jakarta.validation.Valid;

public record EmailSendRequest(
        @EmailValidation
        String email
) {
}
