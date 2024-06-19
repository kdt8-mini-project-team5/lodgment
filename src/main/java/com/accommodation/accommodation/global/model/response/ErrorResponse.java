package com.accommodation.accommodation.global.model.response;

public record ErrorResponse(
        Integer statusCode,
        String message
) {
}
