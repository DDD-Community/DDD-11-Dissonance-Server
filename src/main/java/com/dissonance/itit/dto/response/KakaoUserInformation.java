package com.dissonance.itit.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
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
    public String getProvider() {
        return "kakao";
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
    static class KakaoAccount {
        @JsonProperty(value = "email")
        private String email;
    }
}