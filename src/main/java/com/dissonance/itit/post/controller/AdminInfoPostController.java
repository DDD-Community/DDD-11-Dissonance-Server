package com.dissonance.itit.post.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dissonance.itit.global.common.annotation.CurrentUser;
import com.dissonance.itit.global.common.util.ApiResponse;
import com.dissonance.itit.post.dto.request.InfoPostReq;
import com.dissonance.itit.post.dto.response.InfoPostCreateRes;
import com.dissonance.itit.post.dto.response.InfoPostDetailRes;
import com.dissonance.itit.post.dto.response.InfoPostUpdateRes;
import com.dissonance.itit.post.service.InfoPostFacade;
import com.dissonance.itit.user.domain.User;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/info-posts")
public class AdminInfoPostController {
	private final InfoPostFacade infoPostFacade;

	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@Operation(summary = "공고 게시글 등록", description = "공고 게시글을 등록합니다.")
	public ApiResponse<InfoPostCreateRes> createInfoPost(@RequestPart MultipartFile imgFile,
		@Valid @RequestPart InfoPostReq infoPostReq, @CurrentUser User loginUser) {
		InfoPostCreateRes infoPostCreateRes = infoPostFacade.createInfoPost(imgFile, infoPostReq, loginUser);
		return ApiResponse.success(infoPostCreateRes);
	}

	@DeleteMapping("/{infoPostId}")
	@Operation(summary = "공고 게시글 삭제", description = "공고 게시글을 삭제합니다.")
	public ApiResponse<String> deleteInfoPost(@PathVariable Long infoPostId) {
		infoPostFacade.deleteInfoPostById(infoPostId);
		return ApiResponse.success("게시글 삭제 성공");
	}

	@GetMapping("/{infoPostId}")
	@Operation(summary = "공고 수정 - 게시글 조회", description = "공고 게시글 수정 페이지에 띄울 상세 정보를 조회합니다.")
	public ApiResponse<InfoPostUpdateRes> getInfoPostDetailByIdForUpdate(@PathVariable Long infoPostId) {
		InfoPostUpdateRes infoPostUpdateRes = infoPostFacade.getInfoPostDetailByIdForUpdate(infoPostId);
		return ApiResponse.success(infoPostUpdateRes);
	}

	@PutMapping(value = "/{infoPostId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
		MediaType.APPLICATION_JSON_VALUE})
	@Operation(summary = "공고 게시글 수정", description = "공고 게시글을 수정합니다.")
	public ApiResponse<InfoPostDetailRes> updateInfoPost(@PathVariable Long infoPostId,
		@RequestPart(required = false) MultipartFile imgFile,
		@Valid @RequestPart InfoPostReq infoPostReq, @CurrentUser User loginUser) {
		InfoPostDetailRes infoPostDetailRes = infoPostFacade.updateInfoPostAndImg(infoPostId, imgFile, infoPostReq,
			loginUser);

		return ApiResponse.success(infoPostDetailRes);
	}
}
