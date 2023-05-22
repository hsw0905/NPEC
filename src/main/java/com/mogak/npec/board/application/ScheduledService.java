package com.mogak.npec.board.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Slf4j
public class ScheduledService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final BoardLikeCountSyncService boardLikeCountSyncService;
    private final ObjectMapper mapper;

    public ScheduledService(RedisTemplate<String, Object> redisTemplate, BoardLikeCountSyncService boardLikeCountSyncService, ObjectMapper mapper) {
        this.redisTemplate = redisTemplate;
        this.boardLikeCountSyncService = boardLikeCountSyncService;
        this.mapper = mapper;
    }

    @Scheduled(fixedDelay = 10000)
    public void run() {
        List<Object> boardLikeEvents = redisTemplate.opsForSet().pop("boardLike", 100);

        if (CollectionUtils.isEmpty(boardLikeEvents)) {
            return;
        }

        List<BoardLikeModifiedMessage> messages = convertMessageFromEvent(boardLikeEvents);

        boardLikeCountSyncService.updateBoardSortsLikeCount(messages);
    }

    private List<BoardLikeModifiedMessage> convertMessageFromEvent(List<Object> messages) {
        return messages.stream()
                .map(message -> {
                    try {
                        return mapper.readValue((String) message, BoardLikeModifiedMessage.class);
                    } catch (JsonProcessingException e) {
                        log.error("BoardLikeModifiedMessage.class 로 변환 에러. value: {}", message);
                        throw new RuntimeException(e);
                    }
                }).toList();
    }

}
