package com.mogak.npec.member;

import com.mogak.npec.member.dto.MemberCreateRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {
    @LocalServerPort
    private int port;

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
                .statusCode(HttpStatus.BAD_REQUEST.value());

    }
}
