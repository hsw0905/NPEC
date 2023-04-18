package com.mogak.npec.auth.exception;

import com.mogak.npec.common.exception.BadRequestException;

public class InvalidTokenException extends BadRequestException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
