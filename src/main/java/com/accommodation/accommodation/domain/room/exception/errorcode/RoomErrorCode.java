package com.accommodation.accommodation.domain.room.exception.errorcode;

import com.accommodation.accommodation.global.handler.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class RoomErrorCode implements ErrorCode {

    ;

    private final String statusText;
    private final HttpStatus statusCode;

}
