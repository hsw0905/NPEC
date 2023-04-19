package com.mogak.npec.auth.dto;

import lombok.Getter;

@Getter
public class RefreshResponse {
    private String accessToken;

    public RefreshResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
