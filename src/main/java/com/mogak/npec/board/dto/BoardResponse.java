package com.mogak.npec.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mogak.npec.board.domain.Board;
import com.mogak.npec.hashtag.domain.HashTag;
import com.mogak.npec.hashtag.dto.HashTagListResponse;
import com.mogak.npec.member.dto.MemberResponse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponse {
    private Long id;
    private String title;
    private Long viewCount;
    private Long likeCount;
    // todo 댓글 개수 추가


    @JsonProperty(value = "member")
    private MemberResponse member;

    @JsonProperty(value = "hashtags")
    private HashTagListResponse hashtags;

    private LocalDateTime createdAt;

    public BoardResponse(Long id, String title, Long viewCount, Long likeCount, MemberResponse member, HashTagListResponse hashTags, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.member = member;
        this.hashtags = hashTags;
        this.createdAt = createdAt;
    }

    public static BoardResponse of(Board board, List<HashTag> hashTags) {
        return new BoardResponse(board.getId(), board.getTitle(), board.getViewCount(), board.getLikeCount(), MemberResponse.of(board.getMember()), HashTagListResponse.of(hashTags), board.getCreatedAt());
    }
}
