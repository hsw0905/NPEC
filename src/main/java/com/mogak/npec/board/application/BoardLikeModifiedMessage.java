package com.mogak.npec.board.application;

import lombok.Getter;

@Getter
public class BoardLikeModifiedMessage {
    private Long boardId;

    public BoardLikeModifiedMessage() {
    }

    public BoardLikeModifiedMessage(Long boardId) {
        this.boardId = boardId;
    }
}
