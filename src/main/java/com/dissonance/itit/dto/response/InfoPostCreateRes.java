package com.dissonance.itit.dto.response;

import com.dissonance.itit.domain.entity.InfoPost;
import io.swagger.v3.oas.annotations.media.Schema;

public record InfoPostCreateRes(
        @Schema(name = "게시글 ID", description = "게시글 ID", example = "1")
        Long id,
        @Schema(name = "게시글 제목", description = "게시글 제목", example = "게시글 제목")
        String title
) {
    public static InfoPostCreateRes of(InfoPost infoPost) {
        return new InfoPostCreateRes(infoPost.getId(), infoPost.getTitle());
    }
}
