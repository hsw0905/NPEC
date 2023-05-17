package com.mogak.npec.board.application;

import com.mogak.npec.fixture.MemberFixtureFactory;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.repository.MemberBulkRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardBulkInsertTest {

    @Autowired
    private MemberFixtureFactory memberFixtureFactory;

    @Autowired
    private MemberBulkRepository memberBulkRepository;

    @Test
    void memberTest() {

        StopWatch stopWatch = new StopWatch();

        System.out.println("Bulk Insert Start!!");
        stopWatch.start();

        IntStream.range(0, 10).forEach(i -> memberBulkRepository.bulkInsert(createMembers(10_000 * 10)));

        stopWatch.stop();
        System.out.println("쿼리 실행 시간: " + stopWatch.getTotalTimeSeconds() + "초");
    }

    private List<Member> createMembers(int countPerQuery) {
        EasyRandom easyRandom = memberFixtureFactory.get();

        return IntStream.range(0, countPerQuery)
                .parallel()
                .mapToObj(member -> easyRandom.nextObject(Member.class))
                .toList();
    }


}
