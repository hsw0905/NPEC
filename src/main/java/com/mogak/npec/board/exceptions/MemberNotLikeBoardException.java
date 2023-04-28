package com.mogak.npec.board.exceptions;

import com.mogak.npec.common.exception.BadRequestException;

public class MemberNotLikeBoardException extends BadRequestException {
    public MemberNotLikeBoardException(String message) {
        super(message);
    }
}
