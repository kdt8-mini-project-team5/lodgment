package com.accommodation.accommodation.global.handler.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String getStatusText();

    HttpStatus getStatusCode();

}
