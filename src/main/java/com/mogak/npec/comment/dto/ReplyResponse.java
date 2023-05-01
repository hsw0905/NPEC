package com.mogak.npec.comment.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReplyResponse {
    private Long id;
    private String writer;
    private String content;
    private boolean isBlocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReplyResponse(Long id, String writer, String content, boolean isBlocked, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.isBlocked = isBlocked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
