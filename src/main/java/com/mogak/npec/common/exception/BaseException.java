package com.mogak.npec.common.exception;

import com.mogak.npec.common.dto.ErrorResponse;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    public BaseException(String message) {
        super(message);
    }

}
