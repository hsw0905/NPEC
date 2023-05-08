package com.mogak.npec.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mogak.npec.board.domain.Board;
import com.mogak.npec.hashtag.domain.HashTag;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class BoardListResponse {
    @JsonProperty(value = "boards")
    private List<BoardResponse> boardResponses;
    private int totalPageCount;

    public BoardListResponse(List<BoardResponse> boardResponses, int totalPageCount) {
        this.boardResponses = boardResponses;
        this.totalPageCount = totalPageCount;
    }

    public static BoardListResponse of(List<Board> boards, Map<Long, List<HashTag>> hashTagsByBoardId, int totalPages) {
        List<BoardResponse> boardResponses = boards.stream()
                .map(board -> BoardResponse.of(board, hashTagsByBoardId.getOrDefault(board.getId(), new ArrayList<>())))
                .toList();

        return new BoardListResponse(boardResponses, totalPages);
    }
}
