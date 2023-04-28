package com.mogak.npec.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mogak.npec.board.domain.Board;
import com.mogak.npec.member.dto.MemberResponse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponse {
    private Long id;

    @JsonProperty(value = "member")
    private MemberResponse member;

    private String title;
    private String content;
    private LocalDateTime createdAt;

    public BoardResponse(Long id, MemberResponse member, String title, String content, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static BoardResponse of(Board board) {
        return new BoardResponse(board.getId(), MemberResponse.of(board.getMember()), board.getTitle(), board.getContent(), board.getCreatedAt());
    }
}
