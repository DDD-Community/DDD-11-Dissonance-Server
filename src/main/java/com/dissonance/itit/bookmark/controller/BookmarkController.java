package com.dissonance.itit.bookmark.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dissonance.itit.bookmark.dto.BookmarkToggleRes;
import com.dissonance.itit.bookmark.service.BookmarkService;
import com.dissonance.itit.global.common.util.ApiResponse;
import com.dissonance.itit.global.security.auth.UserContext;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("bookmarks")
public class BookmarkController {
	private final BookmarkService bookmarkService;
	private final UserContext userContext;

	@PostMapping("{infoPostId}/toggle")
	@Operation(summary = "북마크 토글", description = "북마크를 추가/삭제합니다.")
	public ApiResponse<BookmarkToggleRes> toggleBookmark(@PathVariable Long infoPostId) {
		BookmarkToggleRes bookmarkToggleRes = bookmarkService.toggleBookmark(userContext.getUserId(), infoPostId);
		return ApiResponse.success(bookmarkToggleRes);
	}
}
