package com.dissonance.itit.oauth.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dissonance.itit.global.common.util.ApiResponse;
import com.dissonance.itit.global.security.dto.GeneratedToken;
import com.dissonance.itit.oauth.dto.OauthTokenReq;
import com.dissonance.itit.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("oauth")
public class OauthController {
	private final UserService userService;

	@PostMapping("/{provider}")
	@Operation(summary = "소셜 로그인", description = "소셜 로그인 (provider - kakao, apple)")
	public ApiResponse<GeneratedToken> getUserInfos(@PathVariable String provider,
		@Valid @RequestBody OauthTokenReq oauthTokenReq) {
		GeneratedToken token = userService.login(provider, oauthTokenReq.accessToken());

		return ApiResponse.success(token);
	}
}
