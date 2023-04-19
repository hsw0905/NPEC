package com.mogak.npec.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCreateRequest {
    @NotBlank
    @Length(max = 200)
    private String title;

    @NotBlank
    private String content;
}
