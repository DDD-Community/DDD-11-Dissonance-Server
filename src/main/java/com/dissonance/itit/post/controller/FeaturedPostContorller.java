package com.dissonance.itit.post.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dissonance.itit.global.common.util.ApiResponse;
import com.dissonance.itit.post.dto.response.FeaturedPostRes;
import com.dissonance.itit.post.service.FeaturedPostService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("featured-posts")
public class FeaturedPostContorller {
	private final FeaturedPostService featuredPostService;

	@GetMapping
	@Operation(summary = "추천 게시글 조회", description = "운영진 추천 게시글 5개를 조회합니다.")
	public ApiResponse<List<FeaturedPostRes>> getFeaturedPosts() {
		List<FeaturedPostRes> featuredPostRes = featuredPostService.getFeaturedPost();

		return ApiResponse.success(featuredPostRes);
	}

	@PutMapping(value = "{featuredPostId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "추천 게시글 수정", description = "추천 게시글을 수정합니다.")
	public ApiResponse<FeaturedPostRes> updateFeaturedPost(@PathVariable Integer featuredPostId,
		@RequestPart MultipartFile imgFile, @RequestParam Long infoPostId) {
		FeaturedPostRes featuredPostRes = featuredPostService.updateFeaturedPost(featuredPostId, imgFile, infoPostId);

		return ApiResponse.success(featuredPostRes);
	}
}
