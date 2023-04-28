package com.mogak.npec.member.dto;

import com.mogak.npec.member.domain.Member;
import lombok.Getter;

@Getter
public class MemberResponse {
    private Long id;
    private String nickname;

    public MemberResponse(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getNickname());
    }
}
