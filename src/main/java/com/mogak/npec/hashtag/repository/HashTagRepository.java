package com.mogak.npec.hashtag.repository;

import com.mogak.npec.hashtag.domain.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    Optional<HashTag> findByName(String tagName);

    List<HashTag> findAllByNameStartsWith(String name);
}
