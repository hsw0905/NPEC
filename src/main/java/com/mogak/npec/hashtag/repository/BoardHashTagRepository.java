package com.mogak.npec.hashtag.repository;

import com.mogak.npec.hashtag.domain.BoardHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardHashTagRepository extends JpaRepository<BoardHashTag, Long> {
    List<BoardHashTag> findAllByBoardId(Long boardId);

    List<BoardHashTag> findAllByBoardIdIn(List<Long> boardIds);
}
