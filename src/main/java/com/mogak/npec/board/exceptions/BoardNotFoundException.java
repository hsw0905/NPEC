package com.mogak.npec.board.exceptions;

import com.mogak.npec.common.exception.BadRequestException;

public class BoardNotFoundException extends BadRequestException {
    public BoardNotFoundException(String message) {
        super(message);
    }
}
