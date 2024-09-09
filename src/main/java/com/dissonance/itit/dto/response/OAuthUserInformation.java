package com.dissonance.itit.dto.response;

import com.dissonance.itit.domain.enums.SocialLoginProvider;

public interface OAuthUserInformation {
	SocialLoginProvider getProvider();

	String getProviderId();

	String getProfileImgUrl();

	String getNickname();

	String getEmail();
}
