package com.mogak.npec.board.domain;

import com.mogak.npec.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardView extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "boards_id", nullable = false)
    private Board board;

    private Long count;

    public BoardView(Board board, Long count) {
        this.board = board;
        this.count = count;
    }

    public void increaseCount(int i) {
        this.count += i;
    }
}
