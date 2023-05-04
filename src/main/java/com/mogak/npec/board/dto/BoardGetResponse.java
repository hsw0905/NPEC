package com.mogak.npec.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mogak.npec.board.domain.Board;
import com.mogak.npec.member.dto.MemberResponse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardGetResponse {
    private Long id;

    @JsonProperty(value = "member")
    private MemberResponse memberResponse;

    private String title;
    private String content;

    private Long viewCount;
    private Long likeCount;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public BoardGetResponse(Long id, MemberResponse memberResponse, String title, String content, Long viewCount, Long likeCount, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.memberResponse = memberResponse;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static BoardGetResponse of(Board board) {
        return new BoardGetResponse(board.getId(), MemberResponse.of(board.getMember()), board.getTitle(), board.getContent(), board.getViewCount(), board.getLikeCount(), board.getCreatedAt(), board.getModifiedAt());
    }
}
