package com.mogak.npec.auth.application;

import com.mogak.npec.auth.domain.EncryptorImpl;
import com.mogak.npec.auth.application.AuthService;
import com.mogak.npec.auth.domain.TokenProvider;
import com.mogak.npec.auth.dto.LoginRequest;
import com.mogak.npec.auth.dto.LoginTokenResponse;
import com.mogak.npec.auth.exception.LoginFailException;
import com.mogak.npec.auth.repository.BlackListRepository;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AuthServiceTest {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BlackListRepository blackListRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
        blackListRepository.deleteAll();
    }

    @DisplayName("저장된 이메일과 비밀번호로 요청한 경우 멤버 id 를 리턴한다.")
    @Test
    void login() {
        // given
        String email = "member@m.com";
        memberRepository.save(new Member("a", email, new EncryptorImpl().encrypt("1234")));

        // when
        LoginTokenResponse response = authService.login(new LoginRequest(email, "1234"));

        // then
        assertThat(response.getAccessToken()).isNotNull();
        assertThat(response.getRefreshToken()).isNotNull();
    }

    @DisplayName("유효하지 않은 이메일과 비밀번호로 요청한 경우 예외를 던진다.")
    @Test
    void loginWithFail() {
        // given
        String email = "member@m.com";
        memberRepository.save(new Member("a", email, new EncryptorImpl().encrypt("1234")));

        // when
        assertThatThrownBy(
                () -> authService.login(new LoginRequest(email, "11"))
        ).isExactlyInstanceOf(LoginFailException.class);
    }

    @DisplayName("유효한 토큰으로 로그아웃 요청 시 성공")
    @Test
    void logoutSuccess() {
        // given
        String tokenPrefix = "Bearer ";
        String accessToken = tokenProvider.createAccessToken(1L);
        String refreshToken = tokenProvider.createRefreshToken(1L);

        // when
        authService.logout(tokenPrefix + accessToken, tokenPrefix + refreshToken);

    }
}
