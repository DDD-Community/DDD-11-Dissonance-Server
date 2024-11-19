package com.dissonance.itit.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dissonance.itit.common.annotation.CurrentUser;
import com.dissonance.itit.common.util.ApiResponse;
import com.dissonance.itit.domain.entity.User;
import com.dissonance.itit.dto.response.InfoPostDetailRes;
import com.dissonance.itit.dto.response.InfoPostRes;
import com.dissonance.itit.service.InfoPostService;
import com.dissonance.itit.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/info-posts")
public class InfoPostController {
	private final InfoPostService infoPostService;
	private final ReportService reportService;

	@GetMapping("/{infoPostId}")
	@Operation(summary = "공고 게시글 조회", description = "공고 게시글을 상세 조회합니다.")
	public ApiResponse<InfoPostDetailRes> getInfoPostDetail(@PathVariable Long infoPostId) {
		InfoPostDetailRes infoPostDetailRes = infoPostService.getInfoPostDetailById(infoPostId);
		return ApiResponse.success(infoPostDetailRes);
	}

	@PatchMapping("/{infoPostId}/reports")
	@Operation(summary = "공고 게시글 신고", description = "공고 게시글을 신고 처리합니다.")
	public ApiResponse<String> reportedInfoPost(@PathVariable Long infoPostId, @CurrentUser User loginUser) {
		Long resultId = reportService.reportedInfoPost(infoPostId, loginUser);
		return ApiResponse.success(resultId + "번 게시글의 신고가 성공적으로 접수되었습니다.");
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
