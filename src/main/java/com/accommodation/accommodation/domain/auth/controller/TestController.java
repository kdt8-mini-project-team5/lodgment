package com.accommodation.accommodation.domain.auth.controller;

import com.accommodation.accommodation.domain.auth.config.model.CustomUserDetails;
import com.accommodation.accommodation.domain.auth.model.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String singUp(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        System.out.println(customUserDetails);
        return "확인";
    }

}
