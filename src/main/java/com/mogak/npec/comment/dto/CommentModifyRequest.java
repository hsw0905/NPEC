package com.mogak.npec.comment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentModifyRequest {
    private String content;

    public CommentModifyRequest(String content) {
        this.content = content;
    }
}
