package com.mogak.npec.auth.exception;

import com.mogak.npec.common.exception.BadRequestException;

public class TokenExtractFailException extends BadRequestException {
    public TokenExtractFailException(String message) {
        super(message);
    }
}
