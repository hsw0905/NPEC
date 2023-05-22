package com.mogak.npec.hashtag.application;

import com.mogak.npec.board.domain.Board;
import com.mogak.npec.board.repository.BoardRepository;
import com.mogak.npec.hashtag.domain.BoardHashTag;
import com.mogak.npec.hashtag.domain.HashTag;
import com.mogak.npec.hashtag.dto.HashTagGetResponse;
import com.mogak.npec.hashtag.repository.BoardHashTagRepository;
import com.mogak.npec.hashtag.repository.HashTagRepository;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class HashTagServiceTest {

    @Autowired
    private HashTagService hashTagService;

    @Autowired
    private HashTagRepository hashTagRepository;


    @Autowired
    private BoardHashTagRepository boardHashTagRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Board board;
    private HashTag hashTag1;
    private HashTag hashTag2;
    private HashTag hashTag3;
    private HashTag hashTag4;
    private Member member;

    @BeforeEach
    void setUp() {
        hashTag1 = hashTagRepository.save(new HashTag("java"));
        hashTag2 = hashTagRepository.save(new HashTag("spring"));
        hashTag3 = hashTagRepository.save(new HashTag("spring-boot"));
        hashTag4 = hashTagRepository.save(new HashTag("jpa"));

        member = memberRepository.save(new Member("닉네임", "djdj@dldl.com", "1234"));

        board = boardRepository.save(new Board(member, "제목", "내용"));

        boardHashTagRepository.save(new BoardHashTag(board, hashTag1));
        boardHashTagRepository.save(new BoardHashTag(board, hashTag2));
        boardHashTagRepository.save(new BoardHashTag(board, hashTag3));
        boardHashTagRepository.save(new BoardHashTag(board, hashTag4));

    }

    @AfterEach
    void tearDown() {
        boardHashTagRepository.deleteAll();
        hashTagRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @DisplayName("해쉬태그 생성을 요청하는 경우 해쉬태그와 보드해쉬태그 매핑이 저장된다.")
    @Test
    @Transactional
    void createHashTags() {
        // given
        Board savedBoard = boardRepository.save(new Board(member, "제목", "내용"));

        // when
        hashTagService.createHashTags(savedBoard, List.of("java", "spring", "python"));

        // then
        List<BoardHashTag> boardHashTags = boardHashTagRepository.findAllByBoardId(savedBoard.getId());

        Assertions.assertAll(
                () -> assertThat(boardHashTags.size()).isEqualTo(3),
                () -> assertThat(boardHashTags.get(0).getHashTag().getName()).isEqualTo("java"),
                () -> assertThat(boardHashTags.get(1).getHashTag().getName()).isEqualTo("spring"),
                () -> assertThat(boardHashTags.get(2).getHashTag().getName()).isEqualTo("python")
        );
    }

    @DisplayName("게시글 하나의 해쉬태그 리스트를 요청하는 경우 해쉬태그 리스트를 리턴한다.")
    @Test
    @Transactional
    void getHashTagsByBoardId() {
        // when
        List<HashTag> hashTags = hashTagService.getHashTags(board.getId());

        // then
        Assertions.assertAll(
                () -> assertThat(hashTags.size()).isEqualTo(4),
                () -> assertThat(hashTags.get(0).getName()).isEqualTo(hashTag1.getName()),
                () -> assertThat(hashTags.get(1).getName()).isEqualTo(hashTag2.getName()),
                () -> assertThat(hashTags.get(2).getName()).isEqualTo(hashTag3.getName()),
                () -> assertThat(hashTags.get(3).getName()).isEqualTo(hashTag4.getName())
        );
    }

    @DisplayName("게시글 리스트의 해쉬태그 리스트를 요청하는 경우 boardId 로 groupBy 된 해시태그 리스트를 리턴한다.")
    @Test
    @Transactional
    void getHashTagsByBoardIds() {
        // given
        List<String> requestHashTags = List.of("java", "spring", "python");
        Board board2 = boardRepository.save(new Board(member, "제목2", "내용2"));
        hashTagService.createHashTags(board2, requestHashTags);

        // when
        List<Long> boardIds = List.of(board.getId(), board2.getId());
        Map<Long, List<HashTag>> hashTagsByBoardId = hashTagService.getHashTags(boardIds);

        // then
        List<HashTag> hashTahByBoard1 = hashTagsByBoardId.get(board.getId());
        List<HashTag> hashTahByBoard2 = hashTagsByBoardId.get(board2.getId());

        Assertions.assertAll(
                () -> assertThat(hashTagsByBoardId.size()).isEqualTo(2),
                () -> assertThat(hashTahByBoard1.size()).isEqualTo(4),
                () -> assertThat(hashTahByBoard1.get(0).getName()).isEqualTo(hashTag1.getName()),
                () -> assertThat(hashTahByBoard1.get(1).getName()).isEqualTo(hashTag2.getName()),
                () -> assertThat(hashTahByBoard1.get(2).getName()).isEqualTo(hashTag3.getName()),
                () -> assertThat(hashTahByBoard1.get(3).getName()).isEqualTo(hashTag4.getName()),

                () -> assertThat(hashTahByBoard2.size()).isEqualTo(3),
                () -> assertThat(hashTahByBoard2.get(0).getName()).isEqualTo(requestHashTags.get(0)),
                () -> assertThat(hashTahByBoard2.get(1).getName()).isEqualTo(requestHashTags.get(1)),
                () -> assertThat(hashTahByBoard2.get(2).getName()).isEqualTo(requestHashTags.get(2))
        );
    }

    @DisplayName("해쉬태그를 검색하는 경우 리스트를 리턴한다.")
    @Test
    @Transactional
    void searchHashTags() {
        // when
        List<HashTagGetResponse> response = hashTagService.searchWithName("sp").getHashtags();

        // then
        Assertions.assertAll(
                () -> assertThat(response.size()).isEqualTo(2),
                () -> assertThat(response.get(0).getName()).isEqualTo("spring"),
                () -> assertThat(response.get(1).getName()).isEqualTo("spring-boot")
        );
    }

    @DisplayName("기존과 다른 해쉬태그를 추가하여 수정하는 경우 매핑이 수정된다.")
    @Test
    @Transactional
    void updateHashTags() {
        // when
        hashTagService.updateHashTags(board, List.of("java", "spring", "python"));

        // then
        List<BoardHashTag> boardHashTags = boardHashTagRepository.findAllByBoardId(board.getId());

        Assertions.assertAll(
                () -> assertThat(boardHashTags.size()).isEqualTo(3),
                () -> assertThat(boardHashTags.get(0).getHashTag().getName()).isEqualTo("java"),
                () -> assertThat(boardHashTags.get(1).getHashTag().getName()).isEqualTo("spring"),
                () -> assertThat(boardHashTags.get(2).getHashTag().getName()).isEqualTo("python")
        );
    }

    @DisplayName("기존과 다른 해쉬태그를 제거하여 수정하는 경우 매핑이 수정된다.")
    @Test
    @Transactional
    void updateHashTags2() {
        // when
        hashTagService.updateHashTags(board, List.of("java"));

        // then
        List<BoardHashTag> boardHashTags = boardHashTagRepository.findAllByBoardId(board.getId());

        Assertions.assertAll(
                () -> assertThat(boardHashTags.size()).isEqualTo(1),
                () -> assertThat(boardHashTags.get(0).getHashTag().getName()).isEqualTo("java")
        );
    }

    @DisplayName("기존과 같은 해쉬테그를 수정하는 경우 매핑이 기존과 동일하다.")
    @Test
    @Transactional
    void updateHashTags3() {
        // when
        List<String> requestHashTags = List.of("java", "spring", "python");
        hashTagService.updateHashTags(board, requestHashTags);

        // then
        List<BoardHashTag> boardHashTags = boardHashTagRepository.findAllByBoardId(board.getId());

        Assertions.assertAll(
                () -> assertThat(boardHashTags.size()).isEqualTo(3),
                () -> assertThat(boardHashTags.get(0).getHashTag().getName()).isEqualTo(requestHashTags.get(0)),
                () -> assertThat(boardHashTags.get(1).getHashTag().getName()).isEqualTo(requestHashTags.get(1)),
                () -> assertThat(boardHashTags.get(2).getHashTag().getName()).isEqualTo(requestHashTags.get(2))
        );
    }
}
