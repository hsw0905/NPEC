package com.mogak.npec.auth.controller;

import com.mogak.npec.auth.Encryptor;
import com.mogak.npec.auth.EncryptorImpl;
import com.mogak.npec.auth.TokenProvider;
import com.mogak.npec.auth.dto.LoginRequest;
import com.mogak.npec.auth.exception.LoginFailException;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public AuthService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }


    public Long login(LoginRequest request) {
        Encryptor encryptor = new EncryptorImpl();

        Member member = memberRepository.findByEmailAndPassword(request.getEmail(), encryptor.encrypt(request.getPassword()))
                .orElseThrow(
                        () -> new LoginFailException("이메일이나 비밀번호가 잘못되었습니다.")
                );
        return member.getId();
    }

    public String createAccessToken(Long memberId) {
        return tokenProvider.createAccessToken(memberId);
    }

    public String createRefreshToken() {
        return tokenProvider.createRefreshToken();
    }
}
