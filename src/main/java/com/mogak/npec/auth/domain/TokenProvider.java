package com.mogak.npec.auth.domain;


import com.mogak.npec.auth.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
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

    public Long getParsedClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("memberId", Long.class);
        } catch (ExpiredJwtException ex) {
            throw new InvalidTokenException("만료기간이 지난 토큰입니다.");
        } catch (SignatureException ex) {
            throw new InvalidTokenException("믿을 수 없는 토큰입니다.");
        } catch (MalformedJwtException ex) {
            throw new InvalidTokenException("올바른 토큰 형태가 아닙니다.");
        }
    }

    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException exception) {
            return false;
        }
    }
}
