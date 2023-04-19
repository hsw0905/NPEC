package com.mogak.npec.board.controller;

import com.mogak.npec.auth.annotation.ValidToken;
import com.mogak.npec.board.application.BoardService;
import com.mogak.npec.board.dto.BoardCreateRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<Void> createBoard(@ValidToken Long memberId,
                                            @RequestBody @Valid BoardCreateRequest request) {
        Long boardId = boardService.createBoard(memberId, request);
        return ResponseEntity.created(URI.create("/boards/" + boardId)).build();
    }

}
