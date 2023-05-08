package com.mogak.npec.comment.repository;

import com.mogak.npec.comment.domain.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.mogak.npec.comment.domain.QComment.comment;

public class CommentQueryRepositoryImpl implements CommentQueryRepository {
    private final JPAQueryFactory queryFactory;

    public CommentQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Comment> findParentsByBoardId(Long boardId) {
        return queryFactory.selectFrom(comment)
                .where(comment.board.id.eq(boardId).and(comment.parent.isNull()))
                .fetch();
    }
}
