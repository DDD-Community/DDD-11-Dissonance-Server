package com.dissonance.itit.post.dto.response;

import com.dissonance.itit.post.domain.FeaturedPost;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record FeaturedPostRes(
	@Schema(description = "추천 게시글 id", example = "1")
	Integer featuredPostId,
	@Schema(description = "배너 이미지 url", example = "https://naver.com")
	String bannerImageUrl,
	@Schema(description = "게시글 id", example = "1")
	Long infoPostId,
	@Schema(description = "게시글 제목", example = "해커톤")
	String infoPostName) {
	public static FeaturedPostRes of(FeaturedPost featuredPost) {
		return FeaturedPostRes.builder()
			.featuredPostId(featuredPost.getId())
			.bannerImageUrl(featuredPost.getBannerImageUrl())
			.infoPostId(featuredPost.getInfoPost().getId())
			.build();
	}
}
