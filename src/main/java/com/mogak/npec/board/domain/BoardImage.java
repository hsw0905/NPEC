package com.mogak.npec.board.domain;


import com.mogak.npec.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board_images")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "boards_id", nullable = false)
    private Board board;

    @Column(length = 50)
    private String fileName;

    @Column(length = 300)
    private String url;

    public BoardImage(Board board, String fileName, String url) {
        this.board = board;
        this.fileName = fileName;
        this.url = url;
    }
}
