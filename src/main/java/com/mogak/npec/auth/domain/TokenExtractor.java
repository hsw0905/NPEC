package com.mogak.npec.auth.domain;

import org.springframework.stereotype.Component;

@Component
public class TokenExtractor {
    public static final String TOKEN_PREFIX = "Bearer ";

    public String extractAccessToken(String header) {
        if (header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length()).trim();
        }
        return null;
    }
}
