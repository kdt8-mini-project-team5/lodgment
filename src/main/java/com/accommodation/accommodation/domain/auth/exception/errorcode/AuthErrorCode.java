package com.accommodation.accommodation.domain.auth.exception.errorcode;

import com.accommodation.accommodation.global.handler.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    WRONG_EMAIL_SUCCESS_KEY("올바르지 않는 인증 코드 입니다.", HttpStatus.BAD_REQUEST),
    ERROR_SEND_EMAIL("이메일 전송에 실패 했습니다.", HttpStatus.BAD_REQUEST),
    LOGIN_FAILED("로그인에 실패하였습니다.", HttpStatus.BAD_REQUEST)
    ;

    private final String statusText;
    private final HttpStatus statusCode;

}
