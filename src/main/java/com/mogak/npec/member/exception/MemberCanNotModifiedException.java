package com.mogak.npec.member.exception;

import com.mogak.npec.common.exception.BaseException;

public class MemberCanNotModifiedException extends BaseException {
    public MemberCanNotModifiedException(String message) {
        super(message);
    }
}
