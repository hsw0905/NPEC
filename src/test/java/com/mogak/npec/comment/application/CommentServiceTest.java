package com.mogak.npec.comment.application;

import com.mogak.npec.board.domain.Board;
import com.mogak.npec.board.exceptions.BoardCanNotModifyException;
import com.mogak.npec.board.exceptions.BoardNotFoundException;
import com.mogak.npec.board.repository.BoardRepository;
import com.mogak.npec.comment.domain.Comment;
import com.mogak.npec.comment.dto.CreateCommentServiceDto;
import com.mogak.npec.comment.repository.CommentRepository;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.exception.MemberNotFoundException;
import com.mogak.npec.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Board board;

    @BeforeEach
    void setUp() {
        member = new Member("tester", "test@example.com", "1234ab1!");
        board = new Board(member, "제목", "내용", false);

        memberRepository.save(member);
        boardRepository.save(board);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }


    @DisplayName("게시물에 댓글을 생성한다.")
    @Test
    void createCommentSuccess() {
        // given
        String content = "댓글내용";
        CreateCommentServiceDto dto = new CreateCommentServiceDto(member.getId(), board.getId(), content);

        // when
        commentService.createComment(dto);

        // then
        List<Comment> comments = commentRepository.findAll();

        assertAll(
                () -> assertThat(comments.size()).isEqualTo(1),
                () -> assertThat(comments.get(0).getBoard().getId()).isEqualTo(dto.boardId()),
                () -> assertThat(comments.get(0).getMember().getId()).isEqualTo(dto.memberId())
        );
    }

    @DisplayName("사용자가 존재하지 않으면 댓글을 작성할 수 없다.")
    @Test
    void createCommentFail1() {
        // given
        String content = "댓글내용";
        CreateCommentServiceDto dto = new CreateCommentServiceDto(999L, board.getId(), content);

        // when then
        assertThatThrownBy(() -> commentService.createComment(dto))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("게시물이 존재하지 않으면 댓글을 작성할 수 없다.")
    @Test
    void createCommentFail2() {
        // given
        String content = "댓글내용";
        CreateCommentServiceDto dto = new CreateCommentServiceDto(member.getId(), 999L, content);

        // when then
        assertThatThrownBy(() -> commentService.createComment(dto))
                .isInstanceOf(BoardNotFoundException.class);
    }

    @DisplayName("삭제된 게시물엔 댓글을 작성할 수 없다.")
    @Test
    void createCommentFail3() {
        // given
        String content = "댓글내용";
        Board deletedBoard = new Board(member, "제목", "내용", true);
        boardRepository.save(deletedBoard);

        CreateCommentServiceDto dto = new CreateCommentServiceDto(member.getId(), deletedBoard.getId(), content);

        // when then
        assertThatThrownBy(() -> commentService.createComment(dto))
                .isInstanceOf(BoardCanNotModifyException.class);
    }

}
