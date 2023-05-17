package com.mogak.npec.board.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mogak.npec.board.repository.BoardLikeRepository;
import com.mogak.npec.board.repository.BoardSortRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ScheduledService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final BoardSortRepository boardSortRepository;
    private final BoardLikeRepository boardLikeRepository;

    public ScheduledService(RedisTemplate<String, Object> redisTemplate, BoardSortRepository boardSortRepository, BoardLikeRepository boardLikeRepository) {
        this.redisTemplate = redisTemplate;
        this.boardSortRepository = boardSortRepository;
        this.boardLikeRepository = boardLikeRepository;
    }

    @Scheduled(fixedDelay = 10000)
    public void run() {
        List<Object> boardLikeMessagesForString = redisTemplate.opsForSet().pop("boardLike", 100);
        ObjectMapper mapper = new ObjectMapper();

        assert boardLikeMessagesForString != null;
        if (boardLikeMessagesForString.isEmpty()) {
            return;
        }

        List<BoardLikeModifiedMessage> messages = boardLikeMessagesForString.stream()
                .map(it -> {
                    try {
                        return mapper.readValue((String) it, BoardLikeModifiedMessage.class);
                    } catch (JsonProcessingException e) {
                        log.error("BoardLikeModifiedMessage.class 로 변환 에러");
                        throw new RuntimeException();
                    }
                }).toList();

        messages.forEach(
                message -> {
                    Long boardId = message.getBoardId();
                    Long count = boardLikeRepository.countByBoardId(boardId);
                    boardSortRepository.updateLikeCount(count, boardId);
                }
        );
    }
}
