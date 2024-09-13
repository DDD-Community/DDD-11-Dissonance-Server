package com.dissonance.itit.service;

import com.dissonance.itit.domain.enums.SocialLoginProvider;
import com.dissonance.itit.dto.response.OAuthUserInformation;

public interface OAuthService {
	SocialLoginProvider getProvider();

	OAuthUserInformation requestUserInformation(String token);
}
