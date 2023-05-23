package com.mogak.npec.board.application;

import com.mogak.npec.board.repository.BoardLikeRepository;
import com.mogak.npec.board.repository.BoardSortRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardLikeCountSyncService {
    private final BoardSortRepository boardSortRepository;
    private final BoardLikeRepository boardLikeRepository;

    public BoardLikeCountSyncService(BoardSortRepository boardSortRepository, BoardLikeRepository boardLikeRepository) {
        this.boardSortRepository = boardSortRepository;
        this.boardLikeRepository = boardLikeRepository;
    }

    public void updateBoardSortsLikeCount(List<BoardLikeModifiedMessage> messages) {
        messages.forEach(
                message -> {
                    Long boardId = message.getBoardId();
                    Long count = boardLikeRepository.countByBoardId(boardId);
                    boardSortRepository.updateLikeCount(count, boardId);
                }
        );
    }
}
