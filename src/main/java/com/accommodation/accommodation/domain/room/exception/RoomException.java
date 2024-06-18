package com.accommodation.accommodation.domain.room.exception;

import com.accommodation.accommodation.global.handler.exception.errorcode.ErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class RoomException extends HttpStatusCodeException {

    private final ErrorCode errorCode;

    protected RoomException(ErrorCode errorCode) {
        super(errorCode.getStatusCode(), errorCode.getStatusText());
        this.errorCode = errorCode;
    }

}
