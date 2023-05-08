package com.mogak.npec.comment.exception;

import com.mogak.npec.common.exception.BadRequestException;

public class InvalidCommentWriterException extends BadRequestException {
    public InvalidCommentWriterException(String message) {
        super(message);
    }
}
