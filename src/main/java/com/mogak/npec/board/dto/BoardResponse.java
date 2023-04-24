package com.mogak.npec.board.dto;

import com.mogak.npec.board.domain.Board;
import com.mogak.npec.member.dto.MemberResponse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponse {
    private Long id;
    private MemberResponse memberResponse;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public BoardResponse(Long id, MemberResponse memberResponse, String title, String content, LocalDateTime createdAt) {
        this.id = id;
        this.memberResponse = memberResponse;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static BoardResponse of(Board board) {
        return new BoardResponse(board.getId(), MemberResponse.of(board.getMember()), board.getTitle(), board.getContent(), board.getCreatedAt());
    }
}
