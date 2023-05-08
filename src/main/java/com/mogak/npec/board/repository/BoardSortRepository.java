package com.mogak.npec.board.repository;

import com.mogak.npec.board.domain.BoardSort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardSortRepository extends JpaRepository<BoardSort, Long> {
}
