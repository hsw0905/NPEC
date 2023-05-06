package com.mogak.npec.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class MemberUpdateRequest {
    @NotEmpty
    private Long id;
    @NotEmpty
    private String nickname;

    public MemberUpdateRequest(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
