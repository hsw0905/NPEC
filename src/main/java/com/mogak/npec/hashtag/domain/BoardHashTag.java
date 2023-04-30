package com.mogak.npec.hashtag.domain;

import com.mogak.npec.board.domain.Board;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Entity
public class BoardHashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    private HashTag hashTag;

    public BoardHashTag() {
    }

    public BoardHashTag(Board board, HashTag hashTag) {
        this.board = board;
        this.hashTag = hashTag;
    }
}
