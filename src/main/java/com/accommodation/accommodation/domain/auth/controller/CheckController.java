package com.accommodation.accommodation.domain.auth.controller;

import com.accommodation.accommodation.domain.auth.service.CheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CheckController {

    private final CheckService checkService;

    @GetMapping("/check")
    public ResponseEntity<Void> check() {
        return checkService.check();
    }
}