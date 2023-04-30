package com.mogak.npec.hashtag.controller;

import com.mogak.npec.hashtag.application.HashTagService;
import com.mogak.npec.hashtag.dto.HashTagListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hashtags")
public class HashTagController {
    private final HashTagService hashTagService;

    public HashTagController(HashTagService hashTagService) {
        this.hashTagService = hashTagService;
    }

    @GetMapping
    public ResponseEntity<HashTagListResponse> getHashTags(@RequestParam("name") String name) {
        HashTagListResponse response = hashTagService.searchWithName(name);

        return ResponseEntity.ok(response);
    }
}
