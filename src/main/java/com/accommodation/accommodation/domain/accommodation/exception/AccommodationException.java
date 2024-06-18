package com.accommodation.accommodation.domain.accommodation.exception;

import com.accommodation.accommodation.global.handler.exception.errorcode.ErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class AccommodationException extends HttpStatusCodeException {

    private final ErrorCode errorCode;

    protected AccommodationException(ErrorCode errorCode) {
        super(errorCode.getStatusCode(), errorCode.getStatusText());
        this.errorCode = errorCode;
    }

}