package com.mogak.npec.auth.dto;

import lombok.Getter;

@Getter
public class LoginTokenResponse {
    private final String accessToken;
    private final String refreshToken;

    public LoginTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
