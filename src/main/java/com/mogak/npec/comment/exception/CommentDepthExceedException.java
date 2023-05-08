package com.mogak.npec.comment.exception;

import com.mogak.npec.common.exception.BadRequestException;

public class CommentDepthExceedException extends BadRequestException {
    public CommentDepthExceedException(String message) {
        super(message);
    }
}
