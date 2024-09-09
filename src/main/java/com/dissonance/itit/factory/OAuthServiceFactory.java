package com.dissonance.itit.factory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dissonance.itit.common.exception.CustomException;
import com.dissonance.itit.common.exception.ErrorCode;
import com.dissonance.itit.domain.enums.SocialLoginProvider;
import com.dissonance.itit.service.OAuthService;

@Component
public class OAuthServiceFactory {
	private final Map<SocialLoginProvider, OAuthService> oAuthServices;

	@Autowired
	public OAuthServiceFactory(List<OAuthService> oAuthServiceList) {
		oAuthServices = oAuthServiceList.stream()
			.collect(Collectors.toMap(OAuthService::getProvider, Function.identity()));
	}

	public OAuthService getOAuthService(SocialLoginProvider provider) {
		OAuthService service = oAuthServices.get(provider);
		if (service == null) {
			throw new CustomException(ErrorCode.INVALID_PROVIDER);
		}
		return service;
	}
}
