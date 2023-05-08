package com.mogak.npec.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyCreateRequest {
    @NotBlank
    private String content;

    public ReplyCreateRequest(String content) {
        this.content = content;
    }
}
