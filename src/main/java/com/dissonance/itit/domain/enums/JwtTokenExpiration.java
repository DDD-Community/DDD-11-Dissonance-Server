package com.dissonance.itit.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtTokenExpiration {
	ACCESS_TOKEN_EXPIRED_TIME("1시간", 1000L * 60 * 60 * 100000),      // TODO: RT 토큰 도입 후 만료 시간 적용
	REFRESH_TOKEN_EXPIRATION_TIME("2주", 1000L * 60 * 60 * 24 * 14);

	private final String description;
	private final Long value;
}