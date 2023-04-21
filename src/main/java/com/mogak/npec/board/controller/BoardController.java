package com.mogak.npec.board.controller;

import com.mogak.npec.auth.annotation.ValidToken;
import com.mogak.npec.board.application.BoardService;
import com.mogak.npec.board.dto.BoardCreateRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

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

    @PostMapping(value = "/{boardId}/images")
    public ResponseEntity<Void> uploadBoardImages(@RequestPart("file") List<MultipartFile> files, @PathVariable Long boardId) {
        // todo 유저 유효성 검사
        boardService.uploadImages(files, boardId);
        return ResponseEntity.ok().build();
    }
}
