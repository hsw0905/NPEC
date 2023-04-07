package com.mogak.npec.auth.exception;

import com.mogak.npec.common.exception.BadRequestException;

public class LoginFailException extends BadRequestException {
    public LoginFailException(String message) {
        super(message);
    }
}
