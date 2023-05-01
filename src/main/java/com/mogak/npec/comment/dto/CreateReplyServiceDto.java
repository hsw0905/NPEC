package com.mogak.npec.comment.dto;

public record CreateReplyServiceDto (Long memberId, Long commentId, String content) {
}
