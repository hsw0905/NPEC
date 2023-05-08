package com.mogak.npec.comment.domain;

import com.mogak.npec.board.domain.Board;
import com.mogak.npec.common.BaseEntity;
import com.mogak.npec.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boards_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    private Comment(Member member, Board board, Comment parent, String content, boolean isDeleted) {
        this.member = member;
        this.board = board;
        this.parent = parent;
        this.content = content;
        this.isDeleted = isDeleted;
    }

    public static Comment parent(Member member, Board board, String content, boolean isDeleted) {
        return new Comment(member, board, null, content, isDeleted);
    }

    public static Comment child(Member member, Board board, Comment parent, String content, boolean isDeleted) {
        Comment child = new Comment(member, board, parent, content, isDeleted);
        parent.getChildren().add(child);

        return child;
    }

    public boolean isParent() {
        return parent == null;
    }

    public void modifyContent(String content) {
        this.content = content;
    }

    public void deleteComment() {
        isDeleted = true;
        member = null;
    }
}
