package com.dissonance.itit.post.dto.response;

import java.util.List;

import com.dissonance.itit.post.domain.FeaturedPost;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeaturedPostRes {
	@Schema(description = "추천 게시글 id", example = "1")
	private final Integer featuredPostId;
	@Schema(description = "배너 이미지 url", example = "https://naver.com")
	private final String bannerImageUrl;
	@Schema(description = "게시글 id", example = "1")
	private final Long infoPostId;

	public static List<FeaturedPostRes> of(List<FeaturedPost> featuredPosts) {
		return featuredPosts.stream()
			.map(featuredPost -> FeaturedPostRes.builder()
				.featuredPostId(featuredPost.getId())
				.bannerImageUrl(featuredPost.getBannerImageUrl())
				.infoPostId(featuredPost.getInfoPost().getId())
				.build())
			.toList();
	}

	public static FeaturedPostRes of(FeaturedPost featuredPost) {
		return FeaturedPostRes.builder()
			.featuredPostId(featuredPost.getId())
			.bannerImageUrl(featuredPost.getBannerImageUrl())
			.infoPostId(featuredPost.getInfoPost().getId())
			.build();
	}
}
