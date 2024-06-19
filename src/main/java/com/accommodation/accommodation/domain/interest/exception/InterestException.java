package com.accommodation.accommodation.domain.interest.exception;

import com.accommodation.accommodation.global.handler.exception.errorcode.ErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class InterestException extends HttpStatusCodeException {

    private final ErrorCode errorCode;

    protected InterestException(ErrorCode errorCode) {
        super(errorCode.getStatusCode(), errorCode.getStatusText());
        this.errorCode = errorCode;
    }

}

