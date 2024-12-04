package com.dissonance.itit.global.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtTokenExpiration {
	ACCESS_TOKEN_EXPIRED_TIME("1시간", 1000L * 60 * 60 * 1000), // FIXME: 테스트용 임시 연장
	REFRESH_TOKEN_EXPIRATION_TIME("2주", 1000L * 60 * 60 * 24 * 14);

	private final String description;
	private final Long value;
}