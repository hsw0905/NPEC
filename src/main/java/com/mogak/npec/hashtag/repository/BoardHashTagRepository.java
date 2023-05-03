package com.mogak.npec.hashtag.repository;

import com.mogak.npec.board.domain.Board;
import com.mogak.npec.hashtag.domain.BoardHashTag;
import com.mogak.npec.hashtag.domain.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BoardHashTagRepository extends JpaRepository<BoardHashTag, Long> {
    List<BoardHashTag> findAllByBoardId(Long boardId);

    List<BoardHashTag> findAllByBoardIdIn(List<Long> boardIds);

    @Modifying
    @Query("delete from BoardHashTag bh where bh.board = :board and bh.hashTag in (:hashTags)")
    void deleteByBoardIdAndHashTagIdIn(@Param("board") Board board, @Param("hashTags") Collection<HashTag> hashTags);
}
