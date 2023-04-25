package com.mogak.npec.board.application;

import com.mogak.npec.board.domain.Board;
import com.mogak.npec.board.dto.BoardGetResponse;
import com.mogak.npec.board.dto.BoardListResponse;
import com.mogak.npec.board.exceptions.BoardNotFoundException;
import com.mogak.npec.board.repository.BoardRepository;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Board savedBoard;
    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("kim coding", "npec@npec.com", "1234"));
        savedBoard = boardRepository.save(new Board(member, "제목1", "내용1"));
    }

    @AfterEach
    void tearDown() {
        boardRepository.deleteAll();
    }

    @DisplayName("게시판 목록을 요청하면 조회된다.")
    @Test
    void getBoardsWithSuccess() {
        // given
        boardRepository.save(new Board(member, "제목2", "내용2"));

        // when
        BoardListResponse boards = boardService.getBoards(PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt")));

        // then
        assertThat(boards.getBoardResponses().size()).isEqualTo(2);
        assertThat(boards.getTotalPageCount()).isEqualTo(1);
    }

    @DisplayName("게시판 상세조회를 요청하면 조회된다.")
    @Test
    void getBoardWithSuccess() {
        BoardGetResponse findBoard = boardService.getBoard(savedBoard.getId());

        assertAll(
                () -> assertThat(findBoard.getId()).isEqualTo(savedBoard.getId()),
                () -> assertThat(findBoard.getTitle()).isEqualTo(savedBoard.getTitle()),
                () -> assertThat(findBoard.getContent()).isEqualTo(savedBoard.getContent()),
                () -> assertThat(findBoard.getMemberResponse().getId()).isEqualTo(savedBoard.getMember().getId()),
                () -> assertThat(findBoard.getMemberResponse().getNickname()).isEqualTo(savedBoard.getMember().getNickname()),
                () -> assertThat(findBoard.getUpdatedAt()).isEqualTo(savedBoard.getUpdatedAt()),
                () -> assertThat(findBoard.getCreatedAt()).isEqualTo(savedBoard.getUpdatedAt())
        );
    }

    @DisplayName("저장되지 않은 게시판 Id로 상세조회를 요청하면 예외를 던진다.")
    @Test
    void getBoardWithFail() {
        assertThatThrownBy(
                () -> boardService.getBoard(0L)
        ).isExactlyInstanceOf(BoardNotFoundException.class);
    }
}
