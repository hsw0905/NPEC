package com.mogak.npec.member;

import lombok.Getter;

@Getter
public class MemberCreateRequest {
    private String email;
    private String nickname;
    private String password;
}
