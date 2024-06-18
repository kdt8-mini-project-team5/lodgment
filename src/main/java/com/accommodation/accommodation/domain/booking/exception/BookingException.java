package com.accommodation.accommodation.domain.booking.exception;

import com.accommodation.accommodation.global.handler.exception.errorcode.ErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class BookingException extends HttpStatusCodeException {

    private final ErrorCode errorCode;

    protected BookingException(ErrorCode errorCode) {
        super(errorCode.getStatusCode(), errorCode.getStatusText());
        this.errorCode = errorCode;
    }

}
