package com.mogak.npec.comment.application;

import com.mogak.npec.board.domain.Board;
import com.mogak.npec.board.exceptions.BoardCanNotModifyException;
import com.mogak.npec.board.exceptions.BoardNotFoundException;
import com.mogak.npec.board.repository.BoardRepository;
import com.mogak.npec.comment.domain.Comment;
import com.mogak.npec.comment.dto.CommentsResponse;
import com.mogak.npec.comment.dto.CreateCommentServiceDto;
import com.mogak.npec.comment.dto.CreateReplyServiceDto;
import com.mogak.npec.comment.dto.FindCommentsServiceDto;
import com.mogak.npec.comment.exception.CommentCanNotModifyException;
import com.mogak.npec.comment.exception.CommentDepthExceedException;
import com.mogak.npec.comment.exception.CommentNotFoundException;
import com.mogak.npec.comment.repository.CommentRepository;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.exception.MemberNotFoundException;
import com.mogak.npec.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void createComment(CreateCommentServiceDto dto) {
        Member member = findMember(dto.memberId());
        Board board = findBoard(dto.boardId());

        verifyBoard(board);

        Comment comment = Comment.parent(member, board, dto.content(), false, false);

        commentRepository.save(comment);
    }

    @Transactional
    public void createReply(CreateReplyServiceDto dto) {
        Member member = findMember(dto.memberId());
        Comment parent = findComment(dto.commentId());
        Board board = parent.getBoard();

        verifyComment(parent);
        verifyParent(parent);
        verifyBoard(board);

        Comment child = Comment.child(member, board, parent, dto.content(), false, false);

        commentRepository.save(child);
    }

    private void verifyParent(Comment parent) {
        if (!parent.isParent()) {
            throw new CommentDepthExceedException("댓글의 Depth를 초과하였습니다.");
        }
    }

    private void verifyBoard(Board findBoard) {
        if (findBoard.isDeleted()) {
            throw new BoardCanNotModifyException("삭제된 게시물 입니다.");
        }
    }

    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException("게시글을 찾을 수 없습니다.")
        );
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다."));
    }

    private void verifyComment(Comment findComment) {
        if (findComment.isDeleted()) {
            throw new CommentCanNotModifyException("삭제된 댓글 입니다.");
        }
    }

    public CommentsResponse findComments(FindCommentsServiceDto dto) {
        Board board = findBoard(dto.boardId());


        return null;
    }
}
