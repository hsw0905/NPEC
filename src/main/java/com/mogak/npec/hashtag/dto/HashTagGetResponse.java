package com.mogak.npec.hashtag.dto;

import lombok.Getter;

@Getter
public class HashTagGetResponse {
    private Long id;
    private String name;

    public HashTagGetResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
