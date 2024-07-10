package com.accommodation.accommodation.accommodation.model.request;

import com.accommodation.accommodation.domain.accommodation.model.request.AccommodationListRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AccommodationListRequestTest {
    private Validator validator;

    @BeforeEach
    public void AccommodationListRequestSetUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

/*    @Test
    @DisplayName("유효한 AccommodationListRequest 테스트")
    public void validAccommodationListRequest() {
        AccommodationListRequest request = new AccommodationListRequest("관광 호텔", 1000L, 1L, 10);
        Set<ConstraintViolation<AccommodationListRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("유효하지 않은 카테고리 테스트")
    public void invalidCategoryTest() {
        AccommodationListRequest request = new AccommodationListRequest("유효하지 않는 카테고리", 1000L, 1L, 10);
        Set<ConstraintViolation<AccommodationListRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
                violation.getPropertyPath().toString().equals("카텍고리") &&
                        violation.getMessage().contains("유효한 카테고리 요청이 아닙니다.")
        );
    }

    @Test
    @DisplayName("size 필드가 최대값을 초과하는 경우 테스트")
    public void sizeFieldExceedsMaxValue() {
        AccommodationListRequest request = new AccommodationListRequest("관광 호텔", 1000L, 1L, 20);
        Set<ConstraintViolation<AccommodationListRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
                violation.getPropertyPath().toString().equals("size") &&
                        violation.getMessage().contains("size는 최대 20입니다.")
        );
    }*/
}
