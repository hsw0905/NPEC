package com.mogak.npec.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mogak.npec.board.domain.Board;
import com.mogak.npec.hashtag.domain.HashTag;
import com.mogak.npec.hashtag.dto.HashTagListResponse;
import com.mogak.npec.member.dto.MemberResponse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardGetResponse {
    private Long id;

    @JsonProperty(value = "member")
    private MemberResponse memberResponse;

    @JsonProperty(value = "hashtags")
    private HashTagListResponse hashtags;

    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BoardGetResponse(Long id, MemberResponse memberResponse, HashTagListResponse hashtags,String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.memberResponse = memberResponse;
        this.hashtags = hashtags;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BoardGetResponse of(Board board, List<HashTag> hashTags) {
        return new BoardGetResponse(board.getId(), MemberResponse.of(board.getMember()), HashTagListResponse.of(hashTags),board.getTitle(), board.getContent(), board.getCreatedAt(), board.getUpdatedAt());
    }
}
