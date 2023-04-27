package com.mogak.npec.board.dto;

import lombok.Getter;

@Getter
public class BoardUpdateRequest {
    private String title;
    private String content;

    public BoardUpdateRequest() {
    }

    public BoardUpdateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
