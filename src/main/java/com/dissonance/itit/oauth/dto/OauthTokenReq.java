package com.dissonance.itit.oauth.dto;

import jakarta.validation.constraints.NotBlank;

public record OauthTokenReq(
	@NotBlank(message = "access token은 필수 입력입니다.")
	String accessToken
) {
}
