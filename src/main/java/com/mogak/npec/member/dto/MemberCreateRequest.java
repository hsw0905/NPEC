package com.mogak.npec.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class MemberCreateRequest {
    @NotBlank
    @Length(max = 100)
    @Email
    private String email;

    @NotBlank
    @Length(max = 50)
    private String nickname;

    @NotBlank
    @Length(max = 100)
    private String password;

    public MemberCreateRequest() {
    }

    public MemberCreateRequest(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }
}
