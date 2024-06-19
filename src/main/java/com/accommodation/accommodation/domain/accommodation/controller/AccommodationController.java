package com.accommodation.accommodation.domain.accommodation.controller;

import com.accommodation.accommodation.domain.accommodation.model.response.AccommodationResponse;
import com.accommodation.accommodation.domain.accommodation.model.type.Category;
import com.accommodation.accommodation.domain.accommodation.service.AccommodationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accommodation")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping()
    public ResponseEntity findAll(
        @RequestParam("category") String category,
        @RequestParam(value = "cursorMinPrice",required = false) Long cursorMinPrice,
        @RequestParam(value = "cursorId",required = false) Long cursorId,
        @RequestParam("size") int size
        ){
        List<AccommodationResponse> accommodationResponseList = accommodationService.findByCategory(Category.valueOfTypeWithThrow(category), cursorId, PageRequest.of(0, size),cursorMinPrice);
        return ResponseEntity.ok(accommodationResponseList);
    }
}
