package com.mogak.npec.board.repository;

import com.mogak.npec.board.domain.BoardSort;

public interface BoardSortQueryRepository {
    void updateCommentCount(BoardSort boardSort);
}
