package com.mogak.npec.board.repository;

import com.mogak.npec.board.domain.BoardSort;
import com.mogak.npec.board.domain.QBoardSort;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.mogak.npec.board.domain.QBoardSort.*;

public class BoardSortQueryRepositoryImpl implements BoardSortQueryRepository{
    private final JPAQueryFactory queryFactory;

    public BoardSortQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public void updateCommentCount(BoardSort entity) {
        queryFactory.update(boardSort)
                .set(boardSort.commentCount, boardSort.commentCount.add(1))
                .where(boardSort.id.eq(entity.getId()))
                .execute();
    }
}
