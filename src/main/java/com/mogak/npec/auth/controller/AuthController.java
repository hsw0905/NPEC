package com.mogak.npec.auth.controller;

import com.mogak.npec.auth.ValidToken;
import com.mogak.npec.auth.application.AuthService;
import com.mogak.npec.auth.dto.LoginRequest;
import com.mogak.npec.auth.dto.LoginTokenResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginTokenResponse> login(@RequestBody LoginRequest request) {
        LoginTokenResponse response = authService.login(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@ValidToken String accessToken, @ValidToken String refreshToken) {
        authService.logout(accessToken, refreshToken);
        return ResponseEntity.ok().build();
    }
}
