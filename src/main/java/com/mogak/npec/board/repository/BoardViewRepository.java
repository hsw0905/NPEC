package com.mogak.npec.board.repository;

import com.mogak.npec.board.domain.BoardView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardViewRepository extends JpaRepository<BoardView, Long> {
    Optional<BoardView> findByBoardId(Long boardId);
}
