package com.mogak.npec.board.application;

import com.mogak.npec.board.domain.Board;
import com.mogak.npec.board.repository.BoardBulkRepository;
import com.mogak.npec.fixture.BoardFixtureFactory;
import com.mogak.npec.fixture.MemberFixtureFactory;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.repository.MemberBulkRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("local")
public class BoardBulkInsertTest {

    @Autowired
    private MemberFixtureFactory memberFixtureFactory;

    @Autowired
    private MemberBulkRepository memberBulkRepository;

    @Autowired
    private BoardFixtureFactory boardFixtureFactory;

    @Autowired
    private BoardBulkRepository boardBulkRepository;

    @Test
    void bulkCreateMembers() {

        StopWatch stopWatch = new StopWatch();

        System.out.println("Bulk Insert Start!!");
        stopWatch.start();

        IntStream.range(0, 1).forEach(i -> memberBulkRepository.bulkInsert(createMembers(10_000 * 10)));

        stopWatch.stop();
        System.out.println("쿼리 실행 시간: " + stopWatch.getTotalTimeSeconds() + "초");
    }

    @Test
    void bulkCreateBoards() {
        StopWatch stopWatch = new StopWatch();

        System.out.println("Bulk Insert Start!!");
        stopWatch.start();

        IntStream.range(0, 50).forEach(i -> boardBulkRepository.bulkInsert(createBoards(10_000 * 10)));

        stopWatch.stop();
        System.out.println("쿼리 실행 시간: " + stopWatch.getTotalTimeSeconds() + "초");
    }

    private List<Board> createBoards(int countPerQuery) {
        EasyRandom easyRandom = boardFixtureFactory.get();

        return IntStream.range(0, countPerQuery)
                .parallel()
                .mapToObj(member -> easyRandom.nextObject(Board.class))
                .toList();
    }

    private List<Member> createMembers(int countPerQuery) {
        EasyRandom easyRandom = memberFixtureFactory.get();

        return IntStream.range(0, countPerQuery)
                .parallel()
                .mapToObj(member -> easyRandom.nextObject(Member.class))
                .toList();
    }


}
