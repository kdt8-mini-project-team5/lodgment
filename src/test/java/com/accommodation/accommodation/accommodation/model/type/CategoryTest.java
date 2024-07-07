package com.accommodation.accommodation.accommodation.model.type;

import com.accommodation.accommodation.domain.accommodation.model.type.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryTest {
    @Test
    @DisplayName("정의된 category에 대한 유효한 타입 문자열 테스트")
    public void validCategoryCheck(){
        Category category = Category.valueOfType("관광 호텔");
        assertThat(category).isEqualTo(Category.HOTEL);
    }

    @Test
    @DisplayName("유효한 타입 문자열 확인")
    public void validTypeCheck(){
        boolean isValid = Category.checkValidCategory("펜션");
        assertThat(isValid).isTrue();
    }
}
