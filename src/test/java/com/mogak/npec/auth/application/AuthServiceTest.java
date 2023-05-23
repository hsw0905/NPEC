package com.mogak.npec.auth.application;

import com.mogak.npec.auth.domain.BlackList;
import com.mogak.npec.auth.domain.EncryptorImpl;
import com.mogak.npec.auth.domain.TokenProvider;
import com.mogak.npec.auth.dto.LoginRequest;
import com.mogak.npec.auth.dto.LoginTokenResponse;
import com.mogak.npec.auth.dto.RefreshResponse;
import com.mogak.npec.auth.exception.InvalidTokenException;
import com.mogak.npec.auth.exception.LoginFailException;
import com.mogak.npec.auth.repository.BlackListRepository;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {
    private final String EXPIRED_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJtZW1iZXJJZCI6MSwiaWF0IjoxNjgxODAwNDgzLCJleHAiOjE2ODE4MDA1NDN9.Z4vT9gS2I-dT7ljOfPJmtQBWjl5T7806IycG0xRwk58";
    private final String TOKEN_PREFIX = "Bearer ";

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
        Date issuedAt = new Date();
        String accessToken = tokenProvider.createAccessToken(1L, issuedAt);
        String refreshToken = tokenProvider.createRefreshToken(issuedAt);

        // when
        authService.logout(TOKEN_PREFIX + accessToken, TOKEN_PREFIX + refreshToken);

        // then
        assertThat(blackListRepository.findAll().size()).isEqualTo(2);
    }

    @DisplayName("만료된 access 토큰과 유효한 refresh 토큰으로 로그아웃 요청시 성공")
    @Test
    void logoutSuccessWithExpiredToken() {
        // given
        Date issuedAt = new Date();
        String refreshToken = tokenProvider.createRefreshToken(issuedAt);

        // when
        authService.logout(EXPIRED_TOKEN, TOKEN_PREFIX + refreshToken);

        // then
        assertThat(blackListRepository.findAll().size()).isEqualTo(1);
        assertThat(blackListRepository.findById(refreshToken)).isNotEmpty();
    }

    @DisplayName("만료된 access, refresh 토큰으로 로그아웃 요청시 예외를 던진다.")
    @Test
    void logoutFailWithExpiredTokens() {
        assertThatThrownBy(
                () -> authService.logout(EXPIRED_TOKEN, EXPIRED_TOKEN)
        ).isExactlyInstanceOf(InvalidTokenException.class);
    }

    @Test
    void refreshSuccess() {
        // given
        Date issuedAt = new Date();
        String refreshToken = tokenProvider.createRefreshToken(issuedAt);
        String accessToken = tokenProvider.createAccessToken(1L, issuedAt);

        // when
        RefreshResponse response = authService.refresh(TOKEN_PREFIX + refreshToken,
                TOKEN_PREFIX + accessToken);

        // then
        assertThat(response.getAccessToken()).isNotEmpty();
    }

    @DisplayName("만료된 토큰으로 요청한 경우 예외를 던진다.")
    @Test
    void refreshFailWithExpiredToken() {
        Date issuedAt = new Date();
        String accessToken = tokenProvider.createAccessToken(1L, issuedAt);

        assertThatThrownBy(
                () -> authService.refresh(EXPIRED_TOKEN, TOKEN_PREFIX + accessToken)
        ).isExactlyInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("블랙리스트에 저장된 토큰인 경우 예외를 던진다.")
    @Test
    void refreshFailWithBlacklistToken() {
        // given
        Date issuedAt = new Date();
        String refreshToken = tokenProvider.createRefreshToken(issuedAt);
        String accessToken = tokenProvider.createAccessToken(1L, issuedAt);
        blackListRepository.save(new BlackList(refreshToken, 20L));

        assertThatThrownBy(
                () -> authService.refresh(TOKEN_PREFIX + refreshToken, TOKEN_PREFIX + accessToken)
        ).isExactlyInstanceOf(InvalidTokenException.class);
    }
}
