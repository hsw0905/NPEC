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
public class BoardResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    @JsonProperty(value = "member")
    private MemberResponse member;

    @JsonProperty(value = "hashtags")
    private HashTagListResponse hashtags;


    public BoardResponse(Long id, MemberResponse member, String title, String content, LocalDateTime createdAt, HashTagListResponse hashTags) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.hashtags = hashTags;
    }

    public static BoardResponse of(Board board, List<HashTag> hashTags) {
        return new BoardResponse(board.getId(), MemberResponse.of(board.getMember()), board.getTitle(), board.getContent(), board.getCreatedAt(), HashTagListResponse.of(hashTags));
    }
}
