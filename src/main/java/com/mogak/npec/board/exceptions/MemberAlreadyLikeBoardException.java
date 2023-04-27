package com.mogak.npec.board.exceptions;

import com.mogak.npec.common.exception.BadRequestException;

public class MemberAlreadyLikeBoardException extends BadRequestException {
    public MemberAlreadyLikeBoardException(String message) {
        super(message);
    }
}
