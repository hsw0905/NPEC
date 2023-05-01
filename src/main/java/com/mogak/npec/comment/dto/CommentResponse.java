package com.mogak.npec.comment.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CommentResponse {
    private Long id;
    private String writer;
    private String content;
    private boolean isBlocked;
    private List<ReplyResponse> replies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private CommentResponse(Long id, String writer, String content, boolean isBlocked, List<ReplyResponse> replies, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.isBlocked = isBlocked;
        this.replies = replies;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
