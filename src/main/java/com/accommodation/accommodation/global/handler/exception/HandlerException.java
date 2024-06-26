package com.accommodation.accommodation.global.handler.exception;

import com.accommodation.accommodation.domain.accommodation.exception.AccommodationException;
import com.accommodation.accommodation.global.model.response.ErrorResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class HandlerException {

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity authException(HttpStatusCodeException e) {

        log.error(e.getStatusText());

        return ResponseEntity.status(e.getStatusCode())
                .body(new ErrorResponse(e.getStatusCode().value(), e.getStatusText()));
    }

    @ExceptionHandler(AccommodationException.class)
    public ResponseEntity<ErrorResponse> accommodationException(AccommodationException e) {

        log.error(e.getStatusText());

        return ResponseEntity.status(e.getStatusCode())
                .body(new ErrorResponse(e.getStatusCode().value(), e.getStatusText()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(
        MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .toList();
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),String.join(", ", errors)));
    }

}
