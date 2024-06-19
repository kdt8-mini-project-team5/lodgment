package com.accommodation.accommodation.domain.auth.controller;

import com.accommodation.accommodation.domain.auth.model.request.EmailCheckRequest;
import com.accommodation.accommodation.domain.auth.model.request.EmailSendRequest;
import com.accommodation.accommodation.domain.auth.model.request.RegisterRequest;
import com.accommodation.accommodation.domain.auth.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping
    public ResponseEntity singUp(
            @RequestBody RegisterRequest request
    ) {
        return registerService.singUp(request);
    }

    @PostMapping("/email")
    public ResponseEntity emailSend(
            @RequestBody EmailSendRequest request
    ) {
        return registerService.emailSend(request);
    }

    @PostMapping("/email/successKey")
    public ResponseEntity emailCheck(
            @RequestBody EmailCheckRequest request
    ) {
        return registerService.emailCheck(request);
    }

}
