package com.mogak.npec.comment.exception;

import com.mogak.npec.common.exception.BadRequestException;

public class CommentCanNotModifyException extends BadRequestException {
    public CommentCanNotModifyException(String message) {
        super(message);
    }
}
