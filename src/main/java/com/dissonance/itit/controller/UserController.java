package com.dissonance.itit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dissonance.itit.common.annotation.CurrentUser;
import com.dissonance.itit.common.util.ApiResponse;
import com.dissonance.itit.domain.entity.User;
import com.dissonance.itit.dto.response.LoginUserInfoRes;
import com.dissonance.itit.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {
	private final UserService userService;

	@GetMapping
	@Operation(summary = "로그인 유저 정보", description = "로그인 유저의 관리자 여부와 소셜 로그인 provider를 제공합니다.")
	public ApiResponse<LoginUserInfoRes> getUserInfo(@CurrentUser User loginUser) {
		LoginUserInfoRes userInfoRes = userService.getUserInfo(loginUser);

		return ApiResponse.success(userInfoRes);
	}
}
