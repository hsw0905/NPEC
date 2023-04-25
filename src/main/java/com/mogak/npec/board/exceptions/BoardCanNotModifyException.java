package com.mogak.npec.board.exceptions;

import com.mogak.npec.common.exception.BadRequestException;

public class BoardCanNotModifyException extends BadRequestException {
    public BoardCanNotModifyException(String message) {
        super(message);
    }
}
