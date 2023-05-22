package com.mogak.npec.member;

import com.mogak.npec.auth.application.AuthService;
import com.mogak.npec.auth.domain.TokenProvider;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.dto.MemberCreateRequest;
import com.mogak.npec.member.dto.MemberUpdateRequest;
import com.mogak.npec.member.repository.MemberRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MemberControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("잘못된 이메일을 받으면 400을 반환한다.")
    @Test
    void emailValidation() {
        String testEmail = "notEmail";
        String testPassword = "1234";

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new MemberCreateRequest(testEmail, "test", testPassword))
                .post("/members")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("수정할 멤버와 요청하는 멤버가 다르면 예외를 던진다.")
    @Test
    void updateWithDifferentMemberId() {
        Date issuedAt = new Date();
        Member member = memberRepository.save(new Member("kim", "kim@d.com", "1234"));
        String accessToken = tokenProvider.createAccessToken(member.getId(), issuedAt);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .body(new MemberUpdateRequest(0L, "testNickname"))
                .put("/members")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
