package com.mogak.npec.board.dto;

import com.mogak.npec.common.exception.BadRequestException;

public class SortTypeNotFoundException extends BadRequestException {
    public SortTypeNotFoundException(String message) {
        super(message);
    }
}
