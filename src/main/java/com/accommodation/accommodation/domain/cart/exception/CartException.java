package com.accommodation.accommodation.domain.cart.exception;


import com.accommodation.accommodation.global.handler.exception.errorcode.ErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class CartException extends HttpStatusCodeException {

    private final ErrorCode errorCode;

    public CartException(ErrorCode errorCode) {
        super(errorCode.getStatusCode(), errorCode.getStatusText());
        this.errorCode = errorCode;
    }

}
