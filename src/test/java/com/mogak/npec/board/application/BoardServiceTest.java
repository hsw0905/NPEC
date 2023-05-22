package com.mogak.npec.board.application;

import com.mogak.npec.board.domain.Board;
import com.mogak.npec.board.domain.BoardLike;
import com.mogak.npec.board.domain.BoardSort;
import com.mogak.npec.board.dto.BoardGetResponse;
import com.mogak.npec.board.dto.BoardListResponse;
import com.mogak.npec.board.dto.BoardResponse;
import com.mogak.npec.board.dto.BoardUpdateRequest;
import com.mogak.npec.board.exceptions.BoardCanNotModifyException;
import com.mogak.npec.board.exceptions.BoardNotFoundException;
import com.mogak.npec.board.exceptions.MemberAlreadyLikeBoardException;
import com.mogak.npec.board.exceptions.MemberNotLikeBoardException;
import com.mogak.npec.board.repository.BoardLikeRepository;
import com.mogak.npec.board.repository.BoardRepository;
import com.mogak.npec.board.repository.BoardSortRepository;
import com.mogak.npec.hashtag.repository.BoardHashTagRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@ActiveProfiles("test")
class BoardServiceTest {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardLikeRepository boardLikeRepository;
    @Autowired
    private BoardHashTagRepository boardHashTagRepository;
    @Autowired
    private BoardSortRepository boardSortRepository;

    private Board savedBoard;
    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("kim coding", "npec@npec.com", "1234"));
        savedBoard = boardRepository.save(new Board(member, "제목1", "내용1"));
    }

    @AfterEach
    void tearDown() {
        boardSortRepository.deleteAll();
        boardHashTagRepository.deleteAll();
        boardLikeRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @DisplayName("게시판 목록을 요청하면 조회된다. 디폴트 최신순으로")
    @Test
    void getBoardsWithSuccess() {
        // given
        Board board1 = boardRepository.save(new Board(member, "최신순 1등", "내용"));
        Board board2 = boardRepository.save(new Board(member, "최신순 2등", "내용"));

        boardSortRepository.save(new BoardSort(savedBoard, 0L, 0L, 0L));
        boardSortRepository.save(new BoardSort(board2, 0L, 0L, 0L));
        boardSortRepository.save(new BoardSort(board1, 0L, 0L, 0L));

        // when
        BoardListResponse boards = boardService.getBoards(PageRequest.of(0, 3, Sort.by("createdAt").descending()));

        // then
        List<BoardResponse> boardResponses = boards.getBoardResponses();
        assertAll(
                () -> assertThat(boardResponses.get(0).getId()).isEqualTo(board1.getId()),
                () -> assertThat(boardResponses.get(1).getId()).isEqualTo(board2.getId()),
                () -> assertThat(boardResponses.get(2).getId()).isEqualTo(savedBoard.getId())
        );
    }

    @DisplayName("조회수가 높은 수로 정렬한다.")
    @Test
    void getBoardsWithOrderByViewCount() {
        // given
        Board board1 = boardRepository.save(new Board(member, "조회수1등", "내용"));
        Board board2 = boardRepository.save(new Board(member, "조회수2등", "내용"));

        boardSortRepository.save(new BoardSort(savedBoard, 0L, 7L, 0L));
        boardSortRepository.save(new BoardSort(board1, 0L, 10L, 0L));
        boardSortRepository.save(new BoardSort(board2, 0L, 9L, 0L));

        PageRequest pageable = PageRequest.of(0, 3, Sort.by("viewCount").descending());

        // when
        BoardListResponse boards = boardService.getBoards(pageable);

        // then
        List<BoardResponse> boardResponses = boards.getBoardResponses();
        assertAll(
                () -> assertThat(boardResponses.get(0).getId()).isEqualTo(board1.getId()),
                () -> assertThat(boardResponses.get(1).getId()).isEqualTo(board2.getId()),
                () -> assertThat(boardResponses.get(2).getId()).isEqualTo(savedBoard.getId())
        );
    }

    @DisplayName("추천수 높은 수로 정렬한다.")
    @Test
    void getBoardsWithOrderByLikeCount() {
        // given
        Board board1 = boardRepository.save(new Board(member, "추천수 2등", "내용"));
        Board board2 = boardRepository.save(new Board(member, "추천수 1등", "내용"));

        boardSortRepository.save(new BoardSort(savedBoard, 4L, 0L, 0L));
        boardSortRepository.save(new BoardSort(board1, 9L, 0L, 0L));
        boardSortRepository.save(new BoardSort(board2, 11L, 0L, 0L));

        PageRequest pageable = PageRequest.of(0, 3, Sort.by("likeCount").descending());

        // when
        BoardListResponse boards = boardService.getBoards(pageable);

        // then
        List<BoardResponse> boardResponses = boards.getBoardResponses();
        assertAll(
                () -> assertThat(boardResponses.get(0).getId()).isEqualTo(board2.getId()),
                () -> assertThat(boardResponses.get(1).getId()).isEqualTo(board1.getId()),
                () -> assertThat(boardResponses.get(2).getId()).isEqualTo(savedBoard.getId())
        );
    }

    @DisplayName("추천수 높은 수로 정렬한다.")
    @Test
    void getBoardsWithOrderByCommentCount() {
        // given
        Board board1 = boardRepository.save(new Board(member, "댓글수 1등", "내용"));
        Board board2 = boardRepository.save(new Board(member, "댓글수 3등", "내용"));

        boardSortRepository.save(new BoardSort(savedBoard, 0L, 0L, 10L));
        boardSortRepository.save(new BoardSort(board1, 0L, 0L, 14L));
        boardSortRepository.save(new BoardSort(board2, 0L, 0L, 9L));

        PageRequest pageable = PageRequest.of(0, 3, Sort.by("commentCount").descending());

        // when
        BoardListResponse boards = boardService.getBoards(pageable);

        // then
        List<BoardResponse> boardResponses = boards.getBoardResponses();
        assertAll(
                () -> assertThat(boardResponses.get(0).getId()).isEqualTo(board1.getId()),
                () -> assertThat(boardResponses.get(1).getId()).isEqualTo(savedBoard.getId()),
                () -> assertThat(boardResponses.get(2).getId()).isEqualTo(board2.getId())
        );
    }

    @DisplayName("게시판 상세조회를 요청하면 조회된다.")
    @Test
    void getBoardWithSuccess() {
        // given
        BoardSort boardSort = boardSortRepository.save(new BoardSort(savedBoard, 2L, 1L, 3L));

        // when
        BoardGetResponse findBoard = boardService.getBoard(savedBoard.getId());

        // then
        assertAll(
                () -> assertThat(findBoard.getId()).isEqualTo(savedBoard.getId()),
                () -> assertThat(findBoard.getTitle()).isEqualTo(savedBoard.getTitle()),
                () -> assertThat(findBoard.getContent()).isEqualTo(savedBoard.getContent()),
                () -> assertThat(findBoard.getMemberResponse().getId()).isEqualTo(savedBoard.getMember().getId()),
                () -> assertThat(findBoard.getMemberResponse().getNickname()).isEqualTo(savedBoard.getMember().getNickname()),
                () -> assertThat(findBoard.getViewCount()).isEqualTo(boardSort.getViewCount() + 1L),
                () -> assertThat(findBoard.getLikeCount()).isEqualTo(boardSort.getLikeCount()),
                () -> assertThat(findBoard.getCommentCount()).isEqualTo(boardSort.getCommentCount()),
                () -> assertThat(findBoard.getModifiedAt()).isEqualTo(savedBoard.getModifiedAt()),
                () -> assertThat(findBoard.getCreatedAt()).isEqualTo(savedBoard.getCreatedAt())
        );
    }

    @DisplayName("게시판 상세조회를 요청하면 조회수가 증가한다.")
    @Test
    void getBoardWithIncreaseCount() {
        // given
        BoardSort boardSort = boardSortRepository.save(new BoardSort(savedBoard, 2L, 1L, 3L));

        // when
        boardService.getBoard(savedBoard.getId());

        // then
        Long viewCount = boardSortRepository.findById(boardSort.getId()).get().getViewCount();
        assertThat(viewCount).isEqualTo(2L);
    }

    @DisplayName("저장되지 않은 게시판 Id로 상세조회를 요청하면 예외를 던진다.")
    @Test
    void getBoardWithFail() {
        assertThatThrownBy(
                () -> boardService.getBoard(0L)
        ).isExactlyInstanceOf(BoardNotFoundException.class);
    }

    @DisplayName("게시판 수정을 요청하면 수정된다.")
    @Transactional
    @Test
    void updateBoardWithSuccess() {
        // given
        List<String> requestHashTags = List.of("java", "spring", "python");
        BoardUpdateRequest request = new BoardUpdateRequest("수정 후 제목", "수정 후 내용", requestHashTags);

        // when
        boardService.updateBoard(savedBoard.getId(), member.getId(), request);

        // then
        Board findBoard = boardRepository.findById(savedBoard.getId()).get();
        List<String> hashTangNames = boardHashTagRepository.findAllByBoardId(findBoard.getId())
                .stream()
                .map(boardHashTag -> boardHashTag.getHashTag().getName()).toList();

        assertAll(
                () -> assertThat(findBoard.getTitle()).isEqualTo(request.getTitle()),
                () -> assertThat(findBoard.getContent()).isEqualTo(request.getContent()),
                () -> assertThat(findBoard.getModifiedAt()).isNotNull(),
                () -> assertThat(hashTangNames).containsAll(requestHashTags)
        );
    }

    @DisplayName("게시물 작성자가 아닌 멤버가 수정을 요청한 경우 예외를 던진다.")
    @Test
    void updateBoardWithFail() {
        // given
        Member otherMember = memberRepository.save(new Member("kim update", "update@npec.com", "1234"));
        BoardUpdateRequest request = new BoardUpdateRequest("수정 후 제목", "수정 후 내용", new ArrayList<>());

        assertThatThrownBy(
                () -> boardService.updateBoard(savedBoard.getId(), otherMember.getId(), request)
        ).isExactlyInstanceOf(BoardCanNotModifyException.class);
    }


    @DisplayName("삭제된 게시물의 수정을 요청한 경우 예외를 던진다.")
    @Test
    void updateBoardWithDeletedBoard() {
        Board deletedBoard = boardRepository.save(new Board(member, "수정 전 제목", "수정 전 내용", true));
        BoardUpdateRequest request = new BoardUpdateRequest("수정 후 제목", "수정 후 내용", new ArrayList<>());

        assertThatThrownBy(
                () -> boardService.updateBoard(deletedBoard.getId(), member.getId(), request)
        ).isExactlyInstanceOf(BoardCanNotModifyException.class);
    }

    @DisplayName("게시판 삭제를 요청하면 삭제한다.")
    @Test
    void deleteBoardWithSuccess() {
        // when
        boardService.deleteBoard(savedBoard.getId(), member.getId());

        // then
        Board findBoard = boardRepository.findById(savedBoard.getId()).get();
        assertThat(findBoard.getIsDeleted()).isTrue();
    }

    @DisplayName("게시물 작성자가 아닌 멤버가 삭제를 요청한 경우 예외를 던진다.")
    @Test
    void deleteBoardWithFail() {
        // given
        Member otherMember = memberRepository.save(new Member("kim update", "update@npec.com", "1234"));

        assertThatThrownBy(
                () -> boardService.deleteBoard(savedBoard.getId(), otherMember.getId())
        ).isExactlyInstanceOf(BoardCanNotModifyException.class);
    }


    @DisplayName("삭제된 게시물의 삭제를 요청한 경우 예외를 던진다.")
    @Test
    void deleteBoardWithDeletedBoard() {
        Board deletedBoard = boardRepository.save(new Board(member, "수정 전 제목", "수정 전 내용", true));

        assertThatThrownBy(
                () -> boardService.deleteBoard(deletedBoard.getId(), member.getId())
        ).isExactlyInstanceOf(BoardCanNotModifyException.class);
    }

    @DisplayName("게시물을 추천하면 추천 카운트가 1 증가한다.")
    @Test
    void likeBoardSuccess() {
        // given
        BoardSort boardSort = boardSortRepository.save(new BoardSort(savedBoard, 0L, 1L, 0L));
        Member newMember = memberRepository.save(new Member("kim coding2", "npec2@npec.com", "1234"));

        // when
        boardService.likeBoard(savedBoard.getId(), newMember.getId());

        // then
        List<BoardLike> boardLikes = boardLikeRepository.findAll();
        Long likeCount = boardSortRepository.findById(boardSort.getId()).get().getLikeCount();

        assertThat(boardLikes.size()).isEqualTo(1);
        assertThat(likeCount).isEqualTo(1L);
    }

    @DisplayName("같은 사용자가 동일 게시물에 대해 중복 추천 시 익셉션을 던진다.")
    @Test
    void throwExceptionWhenDuplicatedLike() {
        // given
        Member newMember = memberRepository.save(new Member("kim coding2", "npec2@npec.com", "1234"));
        boardSortRepository.save(new BoardSort(savedBoard, 0L, 1L, 0L));
        boardService.likeBoard(savedBoard.getId(), newMember.getId());

        // when then
        assertThatThrownBy(() -> boardService.likeBoard(savedBoard.getId(), newMember.getId()))
                .isInstanceOf(MemberAlreadyLikeBoardException.class);
    }

    @DisplayName("추천 취소시 해당 게시물의 추천 카운트가 1 감소한다.")
    @Test
    void cancelLikeBoardSuccess() {
        // given
        Member newMember = memberRepository.save(new Member("kim coding2", "npec2@npec.com", "1234"));
        BoardSort boardSort = boardSortRepository.save(new BoardSort(savedBoard, 0L, 1L, 0L));
        boardService.likeBoard(savedBoard.getId(), newMember.getId());

        // when
        boardService.cancelLikeBoard(savedBoard.getId(), newMember.getId());

        // then
        List<BoardLike> boardLikes = boardLikeRepository.findAll();
        Long likeCount = boardSortRepository.findById(boardSort.getId()).get().getLikeCount();

        assertThat(boardLikes.size()).isEqualTo(0);
        assertThat(likeCount).isEqualTo(0);
    }

    @DisplayName("추천을 하지 않은 사용자가 추천 취소 요청이 올 경우 익셉션을 던진다.")
    @Test
    void throwExceptionWhenMemberWhoNotCanceledLikeDoCancel() {
        assertThatThrownBy(() -> boardService.cancelLikeBoard(savedBoard.getId(), member.getId()))
                .isInstanceOf(MemberNotLikeBoardException.class);
    }

//    @DisplayName("검색하면 검색 조건에 맞는 게시판 목록이 조회된다.")
//    @Test
//    void searchBoardsWithSuccess() {
//        // given
//        Board board1 = boardRepository.save(new Board(member, "안녕하세요 spring 질문 있습니다.", "너무 재밌어요 ^^"));
//        Board board2 = boardRepository.save(new Board(member, "안녕하세요", "어렵긴한데 spring 할만해요!"));
//        boardRepository.save(new Board(member, "안녕하세요", "출석체크!"));
//
//        // when
//        BoardListResponse response = boardService.searchBoard("spring", PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt")));
//
//        // then
//        List<BoardResponse> searchBoards = response.getBoardResponses();
//        assertAll(
//                () -> assertThat(response.getTotalPageCount()).isEqualTo(1),
//
//                () -> assertThat(searchBoards.size()).isEqualTo(2),
//                () -> assertThat(searchBoards.get(0).getId()).isEqualTo(board2.getId()),
//                () -> assertThat(searchBoards.get(1).getId()).isEqualTo(board1.getId())
//        );
//    }
}
