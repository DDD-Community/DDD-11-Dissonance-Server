package com.dissonance.itit.oauth.dto;

import com.dissonance.itit.oauth.enums.SocialLoginProvider;

public interface OAuthUserInformation {
	SocialLoginProvider getProvider();

	String getProviderId();

	String getProfileImgUrl();

	String getNickname();

	String getEmail();
}
