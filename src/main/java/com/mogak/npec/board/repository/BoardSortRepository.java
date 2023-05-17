package com.mogak.npec.board.repository;

import com.mogak.npec.board.domain.BoardSort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BoardSortRepository extends JpaRepository<BoardSort, Long> {
    Optional<BoardSort> findByBoardId(Long boardId);

    @Transactional
    @Modifying
    @Query("update BoardSort bs set bs.likeCount = :count where bs.board.id = :boardId")
    void updateLikeCount(@Param("count") Long count, @Param("boardId") Long boardId);
}
