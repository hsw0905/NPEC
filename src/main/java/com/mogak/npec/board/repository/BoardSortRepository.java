package com.mogak.npec.board.repository;

import com.mogak.npec.board.domain.BoardSort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardSortRepository extends JpaRepository<BoardSort, Long>, BoardSortQueryRepository {
    Optional<BoardSort> findByBoardId(Long boardId);
}
