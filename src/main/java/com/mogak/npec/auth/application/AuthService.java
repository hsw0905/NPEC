package com.mogak.npec.auth.application;

import com.mogak.npec.auth.domain.BlackList;
import com.mogak.npec.auth.domain.EncryptorImpl;
import com.mogak.npec.auth.domain.TokenExtractor;
import com.mogak.npec.auth.domain.TokenProvider;
import com.mogak.npec.auth.dto.LoginRequest;
import com.mogak.npec.auth.dto.LoginTokenResponse;
import com.mogak.npec.auth.dto.RefreshResponse;
import com.mogak.npec.auth.exception.InvalidTokenException;
import com.mogak.npec.auth.exception.LoginFailException;
import com.mogak.npec.auth.repository.BlackListRepository;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuthService {

    private final Long accessTokenExpire;
    private final Long refreshTokenExpire;

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final EncryptorImpl encryptor;
    private final BlackListRepository blackListRepository;
    private final TokenExtractor tokenExtractor;


    public AuthService(@Value("${redis.ttl.access}") Long accessTokenExpire, @Value("${redis.ttl.refresh}") Long refreshTokenExpire,
                       MemberRepository memberRepository, TokenProvider tokenProvider, EncryptorImpl encryptor, BlackListRepository blackListRepository, TokenExtractor tokenExtractor) {
        this.accessTokenExpire = accessTokenExpire;
        this.refreshTokenExpire = refreshTokenExpire;
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
        this.encryptor = encryptor;
        this.blackListRepository = blackListRepository;
        this.tokenExtractor = tokenExtractor;
    }


    public LoginTokenResponse login(LoginRequest request) {
        Member member = findMember(request);
        Long memberId = member.getId();

        String accessToken = createAccessToken(memberId);
        String refreshToken = createRefreshToken(memberId);

        return new LoginTokenResponse(accessToken, refreshToken);
    }

    public void logout(String accessToken, String refreshToken) {
        String extractedAccessToken = tokenExtractor.extractToken(accessToken);
        String extractedRefreshToken = tokenExtractor.extractToken(refreshToken);

        boolean isValidAccessToken = tokenProvider.isValid(extractedAccessToken);
        boolean isValidRefreshToken = tokenProvider.isValid(extractedRefreshToken);

        if (isValidAccessToken && isValidRefreshToken) {
            blackListRepository.saveAll(List.of(
                    new BlackList(extractedAccessToken, accessTokenExpire),
                    new BlackList(extractedRefreshToken, refreshTokenExpire))
            );
        } else if (isValidRefreshToken) {
            blackListRepository.save(new BlackList(extractedRefreshToken, refreshTokenExpire));

        } else {
            throw new InvalidTokenException("유효한 토큰이 아닙니다.");
        }
    }

    public RefreshResponse refresh(String header) {
        String refreshToken = tokenExtractor.extractToken(header);
        blackListRepository.findById(refreshToken).ifPresent(blackList -> {
            throw new InvalidTokenException("유효하지 않은 refresh token 입니다.");
        });

        Long memberId = tokenProvider.getParsedClaims(refreshToken);
        String newAccessToken = createAccessToken(memberId);

        return new RefreshResponse(newAccessToken);
    }

    private Member findMember(LoginRequest request) {
        return memberRepository.findByEmailAndPassword(request.getEmail(), encryptor.encrypt(request.getPassword()))
                .orElseThrow(
                        () -> new LoginFailException("이메일이나 비밀번호가 잘못되었습니다.")
                );
    }

    private String createAccessToken(Long memberId) {
        Date issuedAt = new Date();
        return tokenProvider.createAccessToken(memberId, issuedAt);
    }

    private String createRefreshToken(Long memberId) {
        Date issuedAt = new Date();
        return tokenProvider.createRefreshToken(memberId, issuedAt);
    }

}
