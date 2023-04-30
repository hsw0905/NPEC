package com.mogak.npec.comment.controller;

import com.mogak.npec.auth.annotation.ValidToken;
import com.mogak.npec.comment.application.CommentService;
import com.mogak.npec.comment.dto.CommentCreateRequest;
import com.mogak.npec.comment.dto.CreateCommentServiceDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/boards/{boardId}/comments")
    public ResponseEntity<Void> createComment(@PathVariable Long boardId, @ValidToken Long memberId,
                                              @Valid @RequestBody CommentCreateRequest request) {

        commentService.createComment(new CreateCommentServiceDto(memberId, boardId, request.getContent()));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
