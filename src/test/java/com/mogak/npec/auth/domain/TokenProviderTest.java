package com.mogak.npec.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;

    @DisplayName("Access Token은 1분간 유효하다.")
    @Test
    void createAccessTokenIsValidIn1minutes() {
        // given
        Date now = new Date();
        Date before1Minute = getBefore1Minute(now);
        Long memberId = 1L;

        // when
        String accessToken = tokenProvider.createAccessToken(memberId, before1Minute);

        // then
        assertThat(tokenProvider.isValid(accessToken)).isFalse();
    }

    @DisplayName("Refresh Token은 14일간 유효하다.")
    @Test
    void createRefreshTokenIsValidIn14Day() {
        // given
        Date now = new Date();
        Date before14days = getBefore14days(now);

        // when
        String refreshToken = tokenProvider.createRefreshToken(before14days);

        // then
        assertThat(tokenProvider.isValid(refreshToken)).isFalse();
    }

    private Date getBefore1Minute(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, -1);

        return calendar.getTime();
    }

    private Date getBefore14days(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, -14);

        return calendar.getTime();
    }
}
