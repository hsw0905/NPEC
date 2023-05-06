package com.mogak.npec.board.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class BoardUpdateRequest {
    private String title;
    private String content;
    private List<String> hashTags;


    public BoardUpdateRequest() {
    }

    public BoardUpdateRequest(String title, String content, List<String> hashTags) {
        this.title = title;
        this.content = content;
        this.hashTags = hashTags;
    }
}
