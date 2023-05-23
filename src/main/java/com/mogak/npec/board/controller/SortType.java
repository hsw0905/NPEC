package com.mogak.npec.board.controller;

import lombok.Getter;

public enum SortType {
    VIEW_COUNT("viewCount"),
    LIKE_COUNT("likeCount"),
    COMMENT_COUNT("commentCount"),
    LATEST("createdAt");

    @Getter
    private final String sortField;

    SortType(String sortField) {
        this.sortField = sortField;
    }
}
