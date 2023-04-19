package com.mogak.npec.auth.domain;

import com.mogak.npec.auth.exception.TokenExtractFailException;
import org.springframework.stereotype.Component;

@Component
public class TokenExtractor {
    public static final String TOKEN_PREFIX = "Bearer ";

    public String extractToken(String header) {
        if (header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length()).trim();
        }
        throw new TokenExtractFailException("추출할 토큰이 없습니다.");
    }
}
