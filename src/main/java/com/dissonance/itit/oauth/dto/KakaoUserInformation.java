package com.dissonance.itit.oauth.dto;

import com.dissonance.itit.oauth.enums.SocialLoginProvider;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@ToString
public class KakaoUserInformation implements OAuthUserInformation {
	@JsonProperty("id")
	private String providerId;
	@JsonProperty("connected_at")
	private String connectedAt;
	@JsonProperty("properties")
	private KakaoOAuthProperties properties;
	@JsonProperty("kakao_account")
	private KakaoAccount kakaoAccount;

	@Override
	public String getNickname() {
		return properties.getNickname();
	}

	@Override
	public String getProfileImgUrl() {
		return properties.thumbnailImage != null ? properties.thumbnailImage : null;
	}

	@Override
	public SocialLoginProvider getProvider() {
		return SocialLoginProvider.KAKAO;
	}

	@Override
	public String getProviderId() {
		return providerId;
	}

	@Override
	public String getEmail() {
		return kakaoAccount.getEmail();
	}

	@ToString
	@Getter
	static class KakaoOAuthProperties {
		@JsonProperty("nickname")
		private String nickname;
		@JsonProperty("profile_image")
		private String profileImage;
		@JsonProperty("thumbnail_image")
		private String thumbnailImage;
	}

	@ToString
	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class KakaoAccount {
		@JsonProperty(value = "email")
		private String email;
		@JsonProperty(value = "profile_image_needs_agreement")
		private Boolean profileImageNeedsAgreement;
		@JsonProperty(value = "profile_nickname_needs_agreement")
		private Boolean profileNicknameNeedsAgreement;
		@JsonProperty(value = "profile")
		private Profile profile;
	}

	@ToString
	@Getter
	static class Profile {
		@JsonProperty("nickname")
		private String nickname;
		@JsonProperty("profile_image_url")
		private String profileImage;
		@JsonProperty("thumbnail_image_url")
		private String thumbnailImage;
		@JsonProperty("is_default_image")
		private Boolean isDefaultImage;
		@JsonProperty("is_default_nickname")
		private Boolean isDefaultNickname;
	}
}
