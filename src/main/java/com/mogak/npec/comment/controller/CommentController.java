package com.mogak.npec.comment.controller;

import com.mogak.npec.auth.annotation.ValidToken;
import com.mogak.npec.comment.application.CommentService;
import com.mogak.npec.comment.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/comments/{commentId}/reply")
    public ResponseEntity<Void> createReply(@PathVariable Long commentId, @ValidToken Long memberId,
                                            @Valid @RequestBody ReplyCreateRequest request) {
        commentService.createReply(new CreateReplyServiceDto(memberId, commentId, request.getContent()));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/boards/{boardId}/comments")
    public ResponseEntity<CommentsResponse> findComments(@PathVariable Long boardId, @ValidToken Long memberId) {
        CommentsResponse response = commentService.findComments(new FindCommentsServiceDto(memberId, boardId));

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentModifyResponse> modifyComment(@PathVariable Long commentId, @ValidToken Long memberId,
                                                               @RequestBody CommentModifyRequest request) {
        CommentModifyResponse response = commentService.modifyComment(new ModifyCommentServiceDto(memberId, commentId,
                request.getContent()));

        return ResponseEntity.ok(response);
    }

}
