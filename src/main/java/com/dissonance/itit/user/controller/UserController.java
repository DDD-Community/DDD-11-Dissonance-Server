package com.dissonance.itit.user.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dissonance.itit.global.common.annotation.CurrentUser;
import com.dissonance.itit.global.common.util.ApiResponse;
import com.dissonance.itit.global.security.dto.GeneratedToken;
import com.dissonance.itit.oauth.dto.RefreshTokenReq;
import com.dissonance.itit.user.domain.User;
import com.dissonance.itit.user.dto.LoginUserInfoRes;
import com.dissonance.itit.user.service.UserService;

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

	@PostMapping("reissue")
	@Operation(summary = "토큰 재발급", description = "access token과 refresh token을 재발급합니다.")
	public ApiResponse<GeneratedToken> reissue(@RequestHeader("Authorization") String requestAccessToken,
		@RequestBody RefreshTokenReq tokenRequest) {
		GeneratedToken reissuedToken = userService.accessTokenByRefreshToken(requestAccessToken,
			tokenRequest.refreshToken());

		return ApiResponse.success(reissuedToken);
	}

	@GetMapping("/logout")
	@Operation(summary = "로그아웃", description = "access token을 만료시킵니다.")
	public ApiResponse<String> logout(@RequestHeader("Authorization") String requestAccessToken) {
		userService.logout(requestAccessToken);

		return ApiResponse.success("로그아웃되었습니다.");
	}

	@DeleteMapping
	@Operation(summary = "회원 탈퇴", description = "로그인 유저의 계정을 탈퇴시킵니다.")
	public ApiResponse<String> withdraw(@CurrentUser User loginUser) {
		userService.withdraw(loginUser.getId());

		return ApiResponse.success("회원 탈퇴가 완료되었습니다.");
	}
}
