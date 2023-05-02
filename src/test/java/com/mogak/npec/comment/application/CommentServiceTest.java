package com.mogak.npec.comment.application;

import com.mogak.npec.board.domain.Board;
import com.mogak.npec.board.exceptions.BoardCanNotModifyException;
import com.mogak.npec.board.exceptions.BoardNotFoundException;
import com.mogak.npec.board.repository.BoardRepository;
import com.mogak.npec.comment.domain.Comment;
import com.mogak.npec.comment.dto.*;
import com.mogak.npec.comment.exception.CommentCanNotModifyException;
import com.mogak.npec.comment.exception.CommentDepthExceedException;
import com.mogak.npec.comment.exception.InvalidCommentWriterException;
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

    @DisplayName("댓글에 대댓글을 생성한다.")
    @Test
    void createReplySuccess() {
        // given
        Comment comment = Comment.parent(member, board, "댓글내용", false);
        commentRepository.save(comment);
        CreateReplyServiceDto dto = new CreateReplyServiceDto(member.getId(), comment.getId(), "대댓글내용");

        // when
        commentService.createReply(dto);

        // then
        List<Comment> comments = commentRepository.findAll();

        assertThat(comments.size()).isEqualTo(2);
        assertThat(comments.get(0).isParent()).isTrue();
        assertThat(comments.get(1).getParent().getId()).isEqualTo(comments.get(0).getId());


    }

    @DisplayName("대댓글엔 다시 대댓글을 생성할 수 없다. (depth: 1)")
    @Test
    void createReplyFail1() {
        // given
        Comment comment = Comment.parent(member, board, "댓글내용", false);
        commentRepository.save(comment);

        CreateReplyServiceDto dto = new CreateReplyServiceDto(member.getId(), comment.getId(), "대댓글내용");
        commentService.createReply(dto);

        List<Comment> comments = commentRepository.findAll();
        Comment child = comments.get(1);

        // when then
        CreateReplyServiceDto dto2 = new CreateReplyServiceDto(member.getId(), child.getId(), "대댓글내용2");
        assertThatThrownBy(() -> commentService.createReply(dto2))
                .isInstanceOf(CommentDepthExceedException.class);

    }

    @DisplayName("삭제된 댓글엔 대댓글을 생성할 수 없다.")
    @Test
    void createReplyFail2() {
        // given
        Comment comment = Comment.parent(member, board, "댓글내용", true);
        commentRepository.save(comment);
        CreateReplyServiceDto dto = new CreateReplyServiceDto(member.getId(), comment.getId(), "대댓글내용");

        // when then
        assertThatThrownBy(() -> commentService.createReply(dto))
                .isInstanceOf(CommentCanNotModifyException.class);

    }

    @DisplayName("댓글을 가져오면 대댓글도 같이 가져온다.")
    @Test
    void findCommentsSuccess() {
        // given
        Comment parent = Comment.parent(member, board, "댓글내용", false);
        Comment child = Comment.child(member, board, parent, "대댓글내용", false);
        parent.getChildren().add(child);

        commentRepository.save(parent);
        commentRepository.save(child);
        FindCommentsServiceDto dto = new FindCommentsServiceDto(member.getId(), board.getId());

        // when
        CommentsResponse comments = commentService.findComments(dto);

        // then
        assertAll(
                () -> assertThat(comments.getCount()).isEqualTo(1),
                () -> assertThat(comments.getComments().get(0).getWriter()).isEqualTo(member.getNickname()),
                () -> assertThat(comments.getComments().get(0).getContent()).isEqualTo(parent.getContent()),
                () -> assertThat(comments.getComments().get(0).getReplies().size()).isEqualTo(1),
                () -> assertThat(comments.getComments().get(0).getReplies().get(0).getContent()).isEqualTo(child.getContent())
        );
    }

    @DisplayName("삭제된 댓글 및 대댓글은 내용을 담지 않는다.")
    @Test
    void findCommentsWithoutDeletedContent() {
        // given
        Comment parent = Comment.parent(member, board, "댓글내용", true);
        Comment child = Comment.child(member, board, parent, "대댓글내용", true);
        parent.getChildren().add(child);

        commentRepository.save(parent);
        commentRepository.save(child);
        FindCommentsServiceDto dto = new FindCommentsServiceDto(member.getId(), board.getId());

        // when
        CommentsResponse comments = commentService.findComments(dto);

        // then
        assertAll(
                () -> assertThat(comments.getCount()).isEqualTo(1),
                () -> assertThat(comments.getComments().get(0).getWriter()).isEqualTo(member.getNickname()),
                () -> assertThat(comments.getComments().get(0).getContent()).isNull(),
                () -> assertThat(comments.getComments().get(0).getReplies().size()).isEqualTo(1),
                () -> assertThat(comments.getComments().get(0).getReplies().get(0).getContent()).isNull()
        );
    }

    @DisplayName("댓글이 없을 땐 response 값이 비어있고, size는 0이다.")
    @Test
    void responseWhenNoComments() {
        // given
        FindCommentsServiceDto dto = new FindCommentsServiceDto(member.getId(), board.getId());

        // when
        CommentsResponse comments = commentService.findComments(dto);

        // then
        assertThat(comments.getCount()).isEqualTo(0);
        assertThat(comments.getComments()).isEmpty();
    }


    @DisplayName("작성자의 수정 요청일 경우 댓글이 수정된다.")
    @Test
    void modifyCommentSuccess() {
        // given
        String modifyContent = "댓글수정내용";
        Comment comment = Comment.parent(member, board, "댓글내용", false);
        commentRepository.save(comment);

        ModifyCommentServiceDto dto = new ModifyCommentServiceDto(member.getId(), comment.getId(), modifyContent);
        // when
        CommentModifyResponse response = commentService.modifyComment(dto);

        // then
        assertThat(response.getContent()).isEqualTo(modifyContent);
        assertThat(response.getWriter()).isEqualTo(member.getNickname());
    }

    @DisplayName("작성자가 아닌 경우 수정 요청이 실패한다.")
    @Test
    void modifyCommentFail1() {
        // given
        Comment comment = Comment.parent(member, board, "댓글내용", false);
        commentRepository.save(comment);

        Member member2 = new Member("tester2", "test2@example.com", "1234ab1!");
        memberRepository.save(member2);

        ModifyCommentServiceDto dto = new ModifyCommentServiceDto(member2.getId(), comment.getId(), "댓글수정내용");

        // when then
        assertThatThrownBy(() -> commentService.modifyComment(dto))
                .isInstanceOf(InvalidCommentWriterException.class);
    }
}
