package com.dissonance.itit.post.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dissonance.itit.global.common.util.ApiResponse;
import com.dissonance.itit.global.security.auth.UserContext;
import com.dissonance.itit.post.dto.response.InfoPostDetailRes;
import com.dissonance.itit.post.dto.response.InfoPostRes;
import com.dissonance.itit.post.service.InfoPostService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/info-posts")
public class InfoPostController {
	private final InfoPostService infoPostService;
	private final UserContext userContext;

	@GetMapping("/{infoPostId}")
	@Operation(summary = "공고 게시글 조회", description = "공고 게시글을 상세 조회합니다.")
	public ApiResponse<InfoPostDetailRes> getInfoPostDetail(@PathVariable Long infoPostId) {
		Long userId = userContext.getUserId();
		InfoPostDetailRes infoPostDetailRes = infoPostService.getInfoPostDetailById(infoPostId, userId);
		return ApiResponse.success(infoPostDetailRes);
	}

	@GetMapping("/categories/{categoryId}/posts")
	@Operation(summary = "공고 게시글 목록 조회", description = "카테고리별 공고 게시글 목록을 조회합니다. (정렬: 최신순 - latest, 마감일순 - deadline)")
	public ApiResponse<Page<InfoPostRes>> getInfoPostsByCategory(@PathVariable Integer categoryId, Pageable pageable) {
		Page<InfoPostRes> infoPostRes = infoPostService.getInfoPostsByCategoryId(categoryId, pageable);

		return ApiResponse.success(infoPostRes);
	}

	@GetMapping("/search")
	@Operation(summary = "공고 게시글 키워드 검색", description = "공고 게시글을 키워드로 검색합니다.")
	public ApiResponse<Page<InfoPostRes>> getInfoPostsByKeyword(@RequestParam String keyword,
		Pageable pageable) {
		Page<InfoPostRes> infoPostRes = infoPostService.getInfoPostsByKeyword(keyword, pageable);

		return ApiResponse.success(infoPostRes);
	}
}
