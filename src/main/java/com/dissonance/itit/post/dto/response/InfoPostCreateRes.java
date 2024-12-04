package com.dissonance.itit.post.dto.response;

import com.dissonance.itit.post.domain.InfoPost;

import io.swagger.v3.oas.annotations.media.Schema;

public record InfoPostCreateRes(
	@Schema(description = "게시글 ID", example = "1")
	Long id,
	@Schema(description = "게시글 제목", example = "게시글 제목")
	String title
) {
	public static InfoPostCreateRes of(InfoPost infoPost) {
		return new InfoPostCreateRes(infoPost.getId(), infoPost.getTitle());
	}
}
