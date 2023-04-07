package com.mogak.npec.member.exception;

import com.mogak.npec.common.exception.BadRequestException;

public class MemberAlreadySavedException extends BadRequestException {

    public MemberAlreadySavedException(String message) {
        super(message);
    }
}
