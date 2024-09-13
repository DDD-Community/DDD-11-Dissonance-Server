package com.dissonance.itit.dto.response;

import com.dissonance.itit.domain.enums.SocialLoginProvider;

public record LoginUserInfoRes(
	boolean isAdmin,
	SocialLoginProvider provider) {
}
