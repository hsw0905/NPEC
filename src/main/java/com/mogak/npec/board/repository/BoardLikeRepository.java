package com.mogak.npec.board.repository;

import com.mogak.npec.board.domain.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    Boolean existsByMemberIdAndBoardId(Long memberId, Long boardId);
    Optional<BoardLike> findByMemberIdAndBoardId(Long memberId, Long boardId);
}
