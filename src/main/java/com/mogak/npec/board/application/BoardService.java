package com.mogak.npec.board.application;

import com.mogak.npec.board.domain.Board;
import com.mogak.npec.board.domain.BoardImage;
import com.mogak.npec.board.dto.BoardCreateRequest;
import com.mogak.npec.board.exceptions.BoardNotFoundException;
import com.mogak.npec.board.repository.BoardImageRepository;
import com.mogak.npec.board.repository.BoardRepository;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.exception.MemberNotFoundException;
import com.mogak.npec.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardImageRepository boardImageRepository;
    private final String imagePath;

    public BoardService(BoardRepository boardRepository, MemberRepository memberRepository, BoardImageRepository boardImageRepository, @Value("${image.path}") String imagePath) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.boardImageRepository = boardImageRepository;
        this.imagePath = imagePath;
    }

    @Transactional
    public Long createBoard(Long memberId, BoardCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        Board board = new Board(member, request.getTitle(), request.getContent());

        Board savedBoard = boardRepository.save(board);

        return savedBoard.getId();
    }

    @Transactional
    public void uploadImages(List<MultipartFile> files, Long boardId) {
        Board findBoard = findBoard(boardId);

        for (MultipartFile image : files) {
            String extension = image.getContentType().split("/")[1];
            String imageName = UUID.randomUUID() + "." + extension;

            boardImageRepository.save(new BoardImage(findBoard, imageName, imagePath + "/" + imageName));

            File file = new File(imagePath, imageName);
            try {
                image.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException("게시글을 찾을 수 없습니다.")
        );
    }
}
