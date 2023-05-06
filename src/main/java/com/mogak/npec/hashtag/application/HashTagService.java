package com.mogak.npec.hashtag.application;

import com.mogak.npec.board.domain.Board;
import com.mogak.npec.hashtag.domain.BoardHashTag;
import com.mogak.npec.hashtag.domain.HashTag;
import com.mogak.npec.hashtag.dto.HashTagListResponse;
import com.mogak.npec.hashtag.repository.BoardHashTagRepository;
import com.mogak.npec.hashtag.repository.HashTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
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

        saveBoardHashTags(board, hashTags);
    }

    private void saveBoardHashTags(Board board, Collection<HashTag> hashTags) {
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

    @Transactional(readOnly = true)
    public HashTagListResponse searchWithName(String name) {
        List<HashTag> findHashTags = hashTagRepository.findAllByNameStartsWith(name.trim());

        return HashTagListResponse.of(findHashTags);
    }

    @Transactional
    public void updateHashTags(Board board, List<String> tagNames) {
        List<HashTag> hashTags = saveOrGetHashTags(tagNames);

        List<HashTag> findHashTags = getHashTags(board.getId());

        updateBoardHashTagMapping(hashTags, findHashTags, board);
    }

    private void updateBoardHashTagMapping(List<HashTag> hashTags, List<HashTag> findHashTags, Board board) {
        HashSet<HashTag> newTags = new HashSet<>(hashTags);
        HashSet<HashTag> oldTags = new HashSet<>(findHashTags);

        if (newTags.containsAll(oldTags) && newTags.size() == oldTags.size()) {
            return;
        }

        deleteBoardHashTagFromOldHashTag(board, newTags, oldTags);
        saveBoardHashTagFromNewHashTag(board, newTags, oldTags);
    }

    private void deleteBoardHashTagFromOldHashTag(Board board, HashSet<HashTag> newTags, HashSet<HashTag> oldTags) {
        HashSet<HashTag> hashTagsForDelete = new HashSet<>();
        for (HashTag oldTag : oldTags) {
            if (!newTags.contains(oldTag)) {
                hashTagsForDelete.add(oldTag);
            }
        }

        boardHashTagRepository.deleteByBoardIdAndHashTagIdIn(board, hashTagsForDelete);
    }

    private void saveBoardHashTagFromNewHashTag(Board board, HashSet<HashTag> newTags, HashSet<HashTag> oldTags) {
        HashSet<HashTag> hashTagsForSave = new HashSet<>();
        for (HashTag newTag : newTags) {
            if (!oldTags.contains(newTag)) {
                hashTagsForSave.add(newTag);
            }
        }
        saveBoardHashTags(board, hashTagsForSave);
    }
}
