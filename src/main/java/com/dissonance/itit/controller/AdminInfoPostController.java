package com.dissonance.itit.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dissonance.itit.common.annotation.CurrentUser;
import com.dissonance.itit.common.util.ApiResponse;
import com.dissonance.itit.domain.entity.User;
import com.dissonance.itit.dto.request.InfoPostReq;
import com.dissonance.itit.dto.response.InfoPostCreateRes;
import com.dissonance.itit.service.InfoPostService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/info-posts")
public class AdminInfoPostController {
	private final InfoPostService infoPostService;

	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@Operation(summary = "공고 게시글 등록", description = "공고 게시글을 등록합니다.")
	public ApiResponse<InfoPostCreateRes> createInfoPost(@RequestPart MultipartFile imgFile,
		@Valid @RequestPart InfoPostReq infoPostReq, @CurrentUser User loginUser) {
		InfoPostCreateRes infoPostCreateRes = infoPostService.createInfoPost(imgFile, infoPostReq, loginUser);
		return ApiResponse.success(infoPostCreateRes);
	}

	@DeleteMapping("/{infoPostId}")
	@Operation(summary = "공고 게시글 삭제", description = "공고 게시글을 삭제합니다.")
	public ApiResponse<String> deleteInfoPost(@PathVariable Long infoPostId) {
		infoPostService.deleteInfoPostById(infoPostId);
		return ApiResponse.success("게시글 삭제 성공");
	}
}
