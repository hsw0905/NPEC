package com.mogak.npec.comment.exception;

import com.mogak.npec.common.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException(String message) {
        super(message);
    }
}
