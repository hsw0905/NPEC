package com.mogak.npec.board.domain;

import com.mogak.npec.common.BaseEntity;
import com.mogak.npec.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "boards", indexes = @Index(name = "idx_created_at", columnList = "created_at"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id", nullable = false)
    private Member member;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    public Board(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public Board(Member member, String title, String content, boolean isDeleted) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.isDeleted = isDeleted;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.modifiedAt = LocalDateTime.now();
    }

    public void delete() {
        this.isDeleted = true;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }
}
