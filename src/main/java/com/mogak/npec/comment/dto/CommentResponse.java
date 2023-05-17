package com.mogak.npec.comment.dto;

import com.mogak.npec.comment.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CommentResponse {
    private Long id;
    private String writer;
    private String content;
    private List<ReplyResponse> replies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private CommentResponse(Long id, String writer, String content, List<ReplyResponse> replies, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.replies = replies;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CommentResponse of(Comment comment, List<ReplyResponse> replies) {
        if (comment.isDeleted()) {
            return new CommentResponse(comment.getId(), null, null,
                    replies, comment.getCreatedAt(), comment.getUpdatedAt());
        }
        return new CommentResponse(comment.getId(), comment.getMember().getNickname(), comment.getContent(),
                replies, comment.getCreatedAt(), comment.getUpdatedAt());
    }

}
