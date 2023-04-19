package com.mogak.npec.auth.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash
@Getter
public class BlackList {
    @Id
    private final String token;
    @TimeToLive
    private final Long ttl;

    public BlackList(String token, Long ttl) {
        this.token = token;
        this.ttl = ttl;
    }
}
