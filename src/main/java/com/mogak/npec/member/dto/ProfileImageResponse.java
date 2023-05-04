package com.mogak.npec.member.dto;

import lombok.Getter;

@Getter
public class ProfileImageResponse {
    private String imagePaths;

    public ProfileImageResponse(String imagePaths) {
        this.imagePaths = imagePaths;
    }
}
