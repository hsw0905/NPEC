package com.mogak.npec.board.dto;


import com.mogak.npec.common.exception.NotFoundException;

public class BoardSortNotFoundException extends NotFoundException {
    public BoardSortNotFoundException(String message) {
        super(message);
    }
}
