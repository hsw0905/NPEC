package com.mogak.npec.board.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class BoardLikeEventHandler {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper mapper;

    public BoardLikeEventHandler(RedisTemplate<String, Object> redisTemplate, ObjectMapper mapper) {
        this.redisTemplate = redisTemplate;
        this.mapper = mapper;
    }

    @TransactionalEventListener
    public void increaseBoardLikeCount(BoardLikeCreatedEvent event) throws JsonProcessingException {
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();
        BoardLikeModifiedMessage message = new BoardLikeModifiedMessage(event.getBoardId());
        String jsonString = mapper.writeValueAsString(message);

        setOps.add("boardLike", jsonString);
    }
}
