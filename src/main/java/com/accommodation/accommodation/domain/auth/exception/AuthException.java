package com.accommodation.accommodation.domain.auth.exception;

import com.accommodation.accommodation.global.handler.exception.errorcode.ErrorCode;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class AuthException extends HttpStatusCodeException {

    private final ErrorCode errorCode;

    public AuthException(ErrorCode errorCode) {
        super(errorCode.getStatusCode(), errorCode.getStatusText());
        this.errorCode = errorCode;
    }

}
