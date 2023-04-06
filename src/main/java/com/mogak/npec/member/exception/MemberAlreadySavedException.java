package com.mogak.npec.member.exception;

import com.mogak.npec.common.exception.BaseException;

public class MemberAlreadySavedException extends BaseException {

    public MemberAlreadySavedException(String errorCode, String message) {
        super(errorCode, message);
    }
}
