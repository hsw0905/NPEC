package com.mogak.npec.auth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {
    private String email;
    private String password;


    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
