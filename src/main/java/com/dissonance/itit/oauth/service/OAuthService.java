package com.dissonance.itit.oauth.service;

import com.dissonance.itit.oauth.dto.OAuthUserInformation;
import com.dissonance.itit.oauth.enums.SocialLoginProvider;

public interface OAuthService {
	SocialLoginProvider getProvider();

	OAuthUserInformation requestUserInformation(String token);
}
