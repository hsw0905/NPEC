package com.mogak.npec.auth.application;

import com.mogak.npec.auth.controller.AuthService;
import com.mogak.npec.auth.dto.LoginRequest;
import com.mogak.npec.auth.dto.LoginTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<LoginTokenResponse> login(@RequestBody LoginRequest request) {
        LoginTokenResponse response = authService.login(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
