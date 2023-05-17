package com.mogak.npec.auth.controller;

import com.mogak.npec.auth.application.AuthService;
import com.mogak.npec.auth.domain.TokenProvider;
import com.mogak.npec.auth.dto.LoginRequest;
import com.mogak.npec.auth.dto.LoginTokenResponse;
import com.mogak.npec.auth.dto.RefreshResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<LoginTokenResponse> login(@RequestBody LoginRequest request) {
        LoginTokenResponse response = authService.login(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken, @RequestHeader("Refresh-Token") String refreshToken) {
        authService.logout(accessToken, refreshToken);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @GetMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestHeader("Refresh-Token") String refreshToken) {
        RefreshResponse response = authService.refresh(refreshToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
