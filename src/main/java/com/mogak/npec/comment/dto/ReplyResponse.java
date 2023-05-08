package com.mogak.npec.comment.dto;

import com.mogak.npec.comment.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReplyResponse {
    private Long id;
    private String writer;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReplyResponse(Long id, String writer, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ReplyResponse of(Comment comment) {
        if (comment.isDeleted()) {
            return new ReplyResponse(comment.getId(), comment.getMember().getNickname(), null,
                    comment.getCreatedAt(), comment.getUpdatedAt());
        }
        return new ReplyResponse(comment.getId(), comment.getMember().getNickname(), comment.getContent(),
                comment.getCreatedAt(), comment.getUpdatedAt());
    }
}
