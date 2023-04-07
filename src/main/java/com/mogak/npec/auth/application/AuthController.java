package com.mogak.npec.auth.application;

import com.mogak.npec.auth.controller.AuthService;
import com.mogak.npec.auth.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

//    @PostMapping("/login")
//    public ResponseEntity<> login(LoginRequest request) {
//        authService.login(request);
//    }
}
