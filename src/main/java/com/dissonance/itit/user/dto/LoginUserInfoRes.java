package com.dissonance.itit.user.dto;

import com.dissonance.itit.oauth.enums.SocialLoginProvider;

public record LoginUserInfoRes(
	boolean isAdmin,
	SocialLoginProvider provider) {
}
