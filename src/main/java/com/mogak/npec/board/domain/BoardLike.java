package com.mogak.npec.board.domain;

import com.mogak.npec.common.BaseEntity;
import com.mogak.npec.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "board_likes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UniqueMemberAndBoard",
                        columnNames = {"members_id", "boards_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "members_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "boards_id")
    private Board board;

    public BoardLike(Member member, Board board) {
        this.member = member;
        this.board = board;
    }
}
