package com.mogak.npec.auth.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash
public class BlackList {
    @Id
    private String token;

    @TimeToLive
    private Long ttl;

    public BlackList(String token, Long ttl) {
        this.token = token;
        this.ttl = ttl;
    }
}
