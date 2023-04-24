package com.mogak.npec.board.application;

import com.mogak.npec.board.domain.Board;
import com.mogak.npec.board.dto.BoardListResponse;
import com.mogak.npec.board.repository.BoardRepository;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    void getBoardsWithSuccess() {
        // given
        Board savedBoard2 = boardRepository.save(new Board(member, "제목2", "내용2"));

        // when
        BoardListResponse boards = boardService.getBoards(PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt")));

        // then
        assertThat(boards.getBoardResponses().size()).isEqualTo(2);
        assertThat(boards.getTotalPageCount()).isEqualTo(1);
    }
}
