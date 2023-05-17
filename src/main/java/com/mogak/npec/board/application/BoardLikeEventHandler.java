package com.mogak.npec.board.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

@Component
public class BoardLikeEventHandler {
    private final RedisTemplate<String, Object> redisTemplate;

    public BoardLikeEventHandler(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @EventListener
    public void increaseBoardLikeCount(BoardLikeCreatedEvent event) throws JsonProcessingException {
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();
        BoardLikeModifiedMessage message = new BoardLikeModifiedMessage(event.getBoardId());

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(message);
        setOps.add("boardLike", jsonString);
    }
}
