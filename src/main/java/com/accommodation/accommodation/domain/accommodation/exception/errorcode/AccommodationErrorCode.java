package com.accommodation.accommodation.domain.accommodation.exception.errorcode;

import com.accommodation.accommodation.global.handler.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccommodationErrorCode implements ErrorCode {

    NOT_FOUND("숙박 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INVALID_DATE("체크인 또는 체크아웃 날짜가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
    ;

    private final String statusText;
    private final HttpStatus statusCode;

}