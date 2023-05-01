package com.mogak.npec.comment.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CommentsResponse {
    private List<CommentResponse> comments;
    private Long count;

    public CommentsResponse(List<CommentResponse> comments) {
        this.comments = comments;
        this.count = (long) comments.size();
    }
}
