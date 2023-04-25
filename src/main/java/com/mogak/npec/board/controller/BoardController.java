package com.mogak.npec.board.controller;

import com.mogak.npec.auth.annotation.ValidToken;
import com.mogak.npec.board.application.BoardService;
import com.mogak.npec.board.dto.BoardCreateRequest;
import com.mogak.npec.board.dto.BoardImageResponse;
import com.mogak.npec.board.dto.BoardGetResponse;
import com.mogak.npec.board.dto.BoardListResponse;
import com.mogak.npec.common.aws.S3Helper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.UUID;

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

    @GetMapping
    public ResponseEntity<BoardListResponse> getBoards(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        BoardListResponse response = boardService.getBoards(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{boardId}")
    public ResponseEntity<BoardGetResponse> getBoard(@PathVariable Long boardId) {
        BoardGetResponse response = boardService.getBoard(boardId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/images")
    public ResponseEntity<BoardImageResponse> uploadBoardImages(@ValidToken Long memberId, @RequestPart("file") List<MultipartFile> files) {
        BoardImageResponse response = boardService.uploadImages(memberId, files);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
