package com.mogak.npec.hashtag.application;

import com.mogak.npec.board.domain.Board;
import com.mogak.npec.hashtag.domain.BoardHashTag;
import com.mogak.npec.hashtag.domain.HashTag;
import com.mogak.npec.hashtag.dto.HashTagListResponse;
import com.mogak.npec.hashtag.repository.BoardHashTagRepository;
import com.mogak.npec.hashtag.repository.HashTagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HashTagService {
    private final HashTagRepository hashTagRepository;
    private final BoardHashTagRepository boardHashTagRepository;

    public HashTagService(HashTagRepository hashTagRepository, BoardHashTagRepository boardHashTagRepository) {
        this.hashTagRepository = hashTagRepository;
        this.boardHashTagRepository = boardHashTagRepository;
    }

    public void createHashTags(Board board, List<String> tagNames) {
        List<HashTag> hashTags = saveOrGetHashTags(tagNames);

        hashTags.stream()
                .map(hashTag -> new BoardHashTag(board, hashTag))
                .forEach(boardHashTagRepository::save);
    }

    public Map<Long, List<HashTag>> getHashTags(List<Long> boardIds) {
        List<BoardHashTag> boardHashTags = boardHashTagRepository.findAllByBoardIdIn(boardIds);

        return boardHashTags.stream()
                .collect(Collectors.groupingBy(
                        boardHashTag -> boardHashTag.getBoard().getId(),
                        Collectors.mapping(BoardHashTag::getHashTag, Collectors.toList())
                ));
    }

    public List<HashTag> getHashTags(Long boardId) {
        List<BoardHashTag> boardHashtags = boardHashTagRepository.findAllByBoardId(boardId);

        return boardHashtags.stream()
                .map(BoardHashTag::getHashTag)
                .collect(Collectors.toList());
    }

    private List<HashTag> saveOrGetHashTags(List<String> tagNames) {
        return tagNames.stream()
                .map(tagName -> hashTagRepository.findByName(tagName.trim()).orElseGet(
                                () -> hashTagRepository.save(new HashTag(tagName))
                        )
                ).collect(Collectors.toList());
    }

    public HashTagListResponse searchWithName(String name) {
        List<HashTag> findHashTags = hashTagRepository.findAllByNameStartsWith(name.trim());

        return HashTagListResponse.of(findHashTags);
    }
}
