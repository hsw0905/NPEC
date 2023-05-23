package com.mogak.npec.member;

import com.mogak.npec.auth.domain.Encryptor;
import com.mogak.npec.auth.domain.EncryptorImpl;
import com.mogak.npec.member.application.MemberService;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.dto.MemberCreateRequest;
import com.mogak.npec.member.dto.MemberUpdateRequest;
import com.mogak.npec.member.exception.MemberAlreadySavedException;
import com.mogak.npec.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @DisplayName("올바른 조건으로 요청하면 회원이 생성된다.")
    @Test
    void createMember() {
        // given
        String testEmail = "test@example.com";
        String testPassword = "1234";
        MemberCreateRequest request = new MemberCreateRequest(testEmail, "who", testPassword);

        // when
        memberService.createMember(request);
        Encryptor encryptor = new EncryptorImpl();
        String encryptPassword = encryptor.encrypt(request.getPassword());

        // then
        Optional<Member> member = memberRepository.findByEmailAndPassword(testEmail, encryptPassword);
        assertThat(member.isPresent()).isTrue();
    }

    @DisplayName("등록된 이메일이 있을 경우 예외를 던진다.")
    @Test
    void notCreatedWhenSameEmail() {
        // given
        String testEmail = "test@example.com";
        String testPassword = "1234";
        MemberCreateRequest request = new MemberCreateRequest(testEmail, "who", testPassword);

        // when
        memberService.createMember(request);

        // then
        assertThatThrownBy(() -> memberService.createMember(request))
                .isInstanceOf(MemberAlreadySavedException.class);
    }

    @DisplayName("멤버 수정을 요청하면 닉네임이 수정된다.")
    @Test
    void updateMemberNickname() {
        // given
        Member member = memberRepository.save(new Member("kim", "kim@d.com", "1234"));
        MemberUpdateRequest request = new MemberUpdateRequest(1L, "updateNickname");

        // when
        memberService.updateMember(member.getId(), request.getNickname());

        // then
        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember.getNickname()).isEqualTo("updateNickname");
    }
}
