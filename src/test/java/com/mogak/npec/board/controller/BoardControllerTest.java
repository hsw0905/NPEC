package com.mogak.npec.board.controller;

import com.mogak.npec.auth.domain.TokenProvider;
import com.mogak.npec.member.repository.MemberRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BoardControllerTest {
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

    @DisplayName("빈 값을 반환한다.")
    @Test
    void success() {
        RestAssured.given().log().all()
                .get("/boards?page=0&sort=VIEW_COUNT")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("잘못된 정렬타입으로 요청하면 400을 리턴한다.")
    @Test
    void sortValidation() {
        RestAssured.given().log().all()
                .get("/boards?page=0&sort=view")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
