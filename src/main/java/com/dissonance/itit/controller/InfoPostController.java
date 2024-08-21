package com.dissonance.itit.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dissonance.itit.domain.entity.User;
import com.dissonance.itit.dto.request.InfoPostReq;
import com.dissonance.itit.dto.response.InfoPostCreateRes;
import com.dissonance.itit.dto.response.InfoPostDetailRes;
import com.dissonance.itit.service.InfoPostService;
import com.dissonance.itit.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/info-posts")
public class InfoPostController {
	private final InfoPostService infoPostService;
	private final UserService userService;

	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@Operation(summary = "공고 게시글 등록", description = "공고 게시글을 등록합니다.")
	public ResponseEntity<InfoPostCreateRes> createInfoPost(@RequestPart MultipartFile imgFile,
		@Valid @RequestPart InfoPostReq infoPostReq) {
		User loginUser = userService.findById(1L);                // TODO: 로그인 유저 정보 적용 예정
		InfoPostCreateRes infoPostCreateRes = infoPostService.createInfoPost(imgFile, infoPostReq, loginUser);
		return ResponseEntity.ok(infoPostCreateRes);
	}

	@GetMapping("/{infoPostId}")
	@Operation(summary = "공고 게시글 조회", description = "공고 게시글을 상세 조회합니다.")
	public ResponseEntity<InfoPostDetailRes> getInfoPostDetail(@PathVariable Long infoPostId) {
		InfoPostDetailRes infoPostDetailRes = infoPostService.getInfoPostDetailById(infoPostId);
		return ResponseEntity.ok(infoPostDetailRes);
	}
}
