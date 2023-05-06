package com.mogak.npec.hashtag.dto;

import com.mogak.npec.hashtag.domain.HashTag;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class HashTagListResponse {
    private List<HashTagGetResponse> hashtags;

    public HashTagListResponse(List<HashTagGetResponse> hashtags) {
        this.hashtags = hashtags;
    }

    public static HashTagListResponse of(List<HashTag> hashTags) {
        List<HashTagGetResponse> hashTagResponses = hashTags.stream()
                .map(hashTag -> new HashTagGetResponse(hashTag.getId(), hashTag.getName()))
                .collect(Collectors.toList());

        return new HashTagListResponse(hashTagResponses);
    }
}
