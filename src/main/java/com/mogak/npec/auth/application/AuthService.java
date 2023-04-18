package com.mogak.npec.auth.application;

import com.mogak.npec.auth.repository.BlackListRepository;
import com.mogak.npec.auth.domain.EncryptorImpl;
import com.mogak.npec.auth.domain.TokenProvider;
import com.mogak.npec.auth.domain.BlackList;
import com.mogak.npec.auth.dto.LoginRequest;
import com.mogak.npec.auth.dto.LoginTokenResponse;
import com.mogak.npec.auth.exception.LoginFailException;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    @Value("${spring.cache.ttl.access}")
    String accessTokenExpire;
    @Value("${spring.cache.ttl.refresh}")
    String refreshTokenExpire;

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final EncryptorImpl encryptor;
    private final BlackListRepository blackListRepository;


    public AuthService(MemberRepository memberRepository, TokenProvider tokenProvider, EncryptorImpl encryptor, BlackListRepository blackListRepository) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
        this.encryptor = encryptor;
        this.blackListRepository = blackListRepository;
    }


    public LoginTokenResponse login(LoginRequest request) {
        Member member = findMember(request);
        Long memberId = member.getId();

        String accessToken = createAccessToken(memberId);
        String refreshToken = createRefreshToken(memberId);

        return new LoginTokenResponse(accessToken, refreshToken);
    }

    public void logout(String accessToken, String refreshToken) {
        blackListRepository.saveAll(List.of(
                new BlackList(accessToken, Long.parseLong(accessTokenExpire)),
                new BlackList(refreshToken, Long.parseLong(refreshTokenExpire)))
        );
    }

    private Member findMember(LoginRequest request) {
        return memberRepository.findByEmailAndPassword(request.getEmail(), encryptor.encrypt(request.getPassword()))
                .orElseThrow(
                        () -> new LoginFailException("이메일이나 비밀번호가 잘못되었습니다.")
                );
    }

    private String createAccessToken(Long memberId) {
        return tokenProvider.createAccessToken(memberId);
    }

    private String createRefreshToken(Long memberId) {
        return tokenProvider.createRefreshToken(memberId);
    }
}
