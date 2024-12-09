package com.dissonance.itit.oauth.dto;

import com.dissonance.itit.oauth.enums.SocialLoginProvider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class AppleUserInfomation implements OAuthUserInformation {
	private String providerId;
	private String email;

	@Override
	public SocialLoginProvider getProvider() {
		return SocialLoginProvider.APPLE;
	}

	@Override
	public String getProviderId() {
		return providerId;
	}

	@Override
	public String getProfileImgUrl() {
		return null;
	}

	@Override
	public String getNickname() {
		return null;
	}
}
