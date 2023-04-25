package com.mogak.npec.board.application;

import com.mogak.npec.board.domain.Board;
import com.mogak.npec.board.dto.BoardCreateRequest;
import com.mogak.npec.board.dto.BoardImageResponse;
import com.mogak.npec.board.dto.BoardGetResponse;
import com.mogak.npec.board.dto.BoardListResponse;
import com.mogak.npec.board.repository.BoardRepository;
import com.mogak.npec.common.aws.S3Helper;
import com.mogak.npec.member.domain.Member;
import com.mogak.npec.member.exception.MemberNotFoundException;
import com.mogak.npec.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    @Transactional(readOnly = true)
    public BoardListResponse getBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);

        return BoardListResponse.of(boards);
    }

    @Transactional(readOnly = true)
    public BoardGetResponse getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException("저장되지 않은 게시글입니다.")
        );
        return BoardGetResponse.of(board);
    }

    @Transactional
    public BoardImageResponse uploadImages(Long memberId, List<MultipartFile> files) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));
        List<String> paths = new ArrayList<>();


        files.forEach(image -> {
            String extension = image.getContentType().split("/")[1];
            String imageName = UUID.randomUUID() + "." + extension;
            String path = member.getEmail() + "/" + BOARD_PATH;

            if (s3Helper.uploadImage(imageName, path, extension, image)) {
                paths.add(s3Helper.getPath(path, imageName));
            }
        });
        return new BoardImageResponse(paths);
    }

    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException("게시글을 찾을 수 없습니다.")
        );
    }
}
