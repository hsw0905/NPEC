package com.mogak.npec.board.application;

import lombok.Getter;

@Getter
public class BoardLikeCreatedEvent {
    private final Long boardId;

    public BoardLikeCreatedEvent(Long boardId) {
        this.boardId = boardId;
    }

    public static BoardLikeCreatedEvent of(Long boardId) {
        return new BoardLikeCreatedEvent(boardId);
    }
}
