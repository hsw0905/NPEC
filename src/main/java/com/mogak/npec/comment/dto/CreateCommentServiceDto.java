package com.mogak.npec.comment.dto;

public record CreateCommentServiceDto(Long memberId, Long boardId, String content) {
}
