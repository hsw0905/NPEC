package com.mogak.npec.auth;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class TokenProvider {
    private final Key secretKey;
    private final Long accessValidityMilliSeconds;
    private final Long refreshValidityMilliSeconds;

    public TokenProvider(@Value("${jwt.key}") String secretKey,
                         @Value("${jwt.expire-milliseconds.access}") Long accessValidityMilliSeconds,
                         @Value("${jwt.expire-milliseconds.refresh}") Long refreshValidityMilliSeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessValidityMilliSeconds = accessValidityMilliSeconds;
        this.refreshValidityMilliSeconds = refreshValidityMilliSeconds;
    }

    public String createAccessToken(Long memberId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessValidityMilliSeconds);

        return Jwts.builder()
                .claim("memberId", memberId)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(Long memberId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshValidityMilliSeconds);

        return Jwts.builder()
                .claim("memberId", memberId)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey)
                .compact();
    }
}
