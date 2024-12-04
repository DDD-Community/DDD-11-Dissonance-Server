package com.dissonance.itit.global.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GeneratedToken {
	private String accessToken;
	private String refreshToken;

	@Builder
	public GeneratedToken(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
