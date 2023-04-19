package com.mogak.npec.member.controller;

import com.mogak.npec.member.dto.MemberCreateRequest;
import com.mogak.npec.member.application.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberCreateRequest request) {
        memberService.createMember(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
