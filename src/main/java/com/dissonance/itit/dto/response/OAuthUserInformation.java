package com.dissonance.itit.dto.response;

public interface OAuthUserInformation {
    String getProvider();
    String getProviderId();
    String getProfileImgUrl();
    String getNickname();
    String getEmail();
}
