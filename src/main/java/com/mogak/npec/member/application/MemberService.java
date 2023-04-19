package com.mogak.npec.member.application;

import com.mogak.npec.auth.domain.Encryptor;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.dto.MemberCreateRequest;
import com.mogak.npec.member.exception.MemberAlreadySavedException;
import com.mogak.npec.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final Encryptor encryptor;

    public MemberService(MemberRepository memberRepository, Encryptor encryptor) {
        this.memberRepository = memberRepository;
        this.encryptor = encryptor;
    }

    @Transactional
    public void createMember(MemberCreateRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new MemberAlreadySavedException("이미 등록된 이메일입니다.");
        }
        String encryptPassword = encryptor.encrypt(request.getPassword());

        memberRepository.save(new Member(request.getNickname(), request.getEmail(), encryptPassword));
    }
}
