package com.mogak.npec.member.domain;

import com.mogak.npec.common.BaseEntity;
import com.mogak.npec.member.dto.MemberCreateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "members")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String nickname;

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String password;

    private boolean isOut;

    public Member() {
    }

    public Member(MemberCreateRequest request) {
        this.nickname = request.getNickname();
        this.email = request.getEmail();
        this.password = request.getPassword();
    }
}
