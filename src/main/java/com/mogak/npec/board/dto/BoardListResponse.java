package com.mogak.npec.board.dto;

import com.mogak.npec.board.domain.Board;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class BoardListResponse {
    private List<BoardResponse> boardResponses;
    private int totalPageCount;

    public BoardListResponse(List<BoardResponse> boardResponses, int totalPageCount) {
        this.boardResponses = boardResponses;
        this.totalPageCount = totalPageCount;
    }

    public static BoardListResponse of(Page<Board> boards) {
        List<BoardResponse> boardResponses = boards.stream()
                .map(BoardResponse::of)
                .toList();

        return new BoardListResponse(boardResponses, boards.getTotalPages());
    }
}
