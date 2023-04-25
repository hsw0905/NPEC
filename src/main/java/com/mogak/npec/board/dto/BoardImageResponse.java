package com.mogak.npec.board.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class BoardImageResponse {
    private List<String> imagePaths;

    public BoardImageResponse(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}
