package com.mogak.npec.member.controller;

import com.mogak.npec.auth.annotation.ValidToken;
import com.mogak.npec.member.application.MemberService;
import com.mogak.npec.member.dto.MemberCreateRequest;
import com.mogak.npec.member.dto.MemberUpdateRequest;
import com.mogak.npec.member.dto.ProfileImageResponse;
import com.mogak.npec.member.exception.MemberCanNotModifiedException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

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

    @PutMapping
    public ResponseEntity<Void> updateMember(@ValidToken Long memberId, @RequestBody @Valid MemberUpdateRequest request) {
        verifyMember(memberId, request.getId());
        memberService.updateMember(memberId, request.getNickname());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private void verifyMember(Long memberId, Long id) {
        if (!Objects.equals(memberId, id)) {
            throw new MemberCanNotModifiedException("요청하는 멤버와 수정할 멤버가 다릅니다.");
        }
    }

    @PutMapping("/imageUrl")
    public ResponseEntity<ProfileImageResponse> updateImageUrl(@ValidToken Long memberId, @RequestPart("file") MultipartFile file) {
        ProfileImageResponse response = memberService.updateProfileImage(memberId, file);

        return ResponseEntity.ok(response);
    }
}
