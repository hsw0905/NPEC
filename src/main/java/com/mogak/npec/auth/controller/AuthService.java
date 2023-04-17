package com.mogak.npec.auth.controller;

import com.mogak.npec.auth.EncryptorImpl;
import com.mogak.npec.auth.TokenProvider;
import com.mogak.npec.auth.dto.LoginRequest;
import com.mogak.npec.auth.dto.LoginTokenResponse;
import com.mogak.npec.auth.exception.LoginFailException;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final EncryptorImpl encryptor;

    public AuthService(MemberRepository memberRepository, TokenProvider tokenProvider, EncryptorImpl encryptor) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
        this.encryptor = encryptor;
    }


    public LoginTokenResponse login(LoginRequest request) {
        Member member = findMember(request);
        Long memberId = member.getId();

        String accessToken = createAccessToken(memberId);
        String refreshToken = createRefreshToken(memberId);

        return new LoginTokenResponse(accessToken, refreshToken);
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
