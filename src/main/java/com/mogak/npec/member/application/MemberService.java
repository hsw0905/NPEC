package com.mogak.npec.member.application;

import com.mogak.npec.auth.Encryptor;
import com.mogak.npec.auth.EncryptorImpl;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.dto.MemberCreateRequest;
import com.mogak.npec.member.exception.MemberAlreadySavedException;
import com.mogak.npec.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void createMember(MemberCreateRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new MemberAlreadySavedException("이미 등록된 이메일입니다.");
        }
        Encryptor encryptor = new EncryptorImpl();
        String encryptPassword = encryptor.encrypt(request.getPassword());

        memberRepository.save(new Member(request.getNickname(), request.getEmail(), encryptPassword));
    }
}
