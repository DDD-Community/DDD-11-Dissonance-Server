package com.dissonance.itit.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dissonance.itit.common.exception.CustomException;
import com.dissonance.itit.common.exception.ErrorCode;
import com.dissonance.itit.common.jwt.util.JwtUtil;
import com.dissonance.itit.domain.entity.User;
import com.dissonance.itit.domain.enums.Role;
import com.dissonance.itit.domain.enums.SocialLoginProvider;
import com.dissonance.itit.dto.response.GeneratedToken;
import com.dissonance.itit.dto.response.LoginUserInfoRes;
import com.dissonance.itit.dto.response.OAuthUserInformation;
import com.dissonance.itit.factory.OAuthServiceFactory;
import com.dissonance.itit.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	private final OAuthServiceFactory oAuthServiceFactory;

	@Transactional
	public GeneratedToken login(String provider, String token) {
		SocialLoginProvider providerEnum = SocialLoginProvider.valueOf(provider.toUpperCase());
		OAuthService oAuthService = oAuthServiceFactory.getOAuthService(providerEnum);

		OAuthUserInformation userInformation = oAuthService.requestUserInformation(token);

		User user;

		if (isExistsByProviderAndProviderId(providerEnum, userInformation.getProviderId())) {
			log.info("[UserService] login, response: {}", userInformation);
			user = findByEmail(userInformation.getEmail());
		} else {
			log.info("[UserService] signUp, response: {}", userInformation);
			user = saveUser(userInformation);
		}

		return jwtUtil.generateToken(user.getEmail(), user.getRole().getKey());
	}

	private boolean isExistsByProviderAndProviderId(SocialLoginProvider provider, String providerId) {
		return userRepository.existsByProviderAndProviderId(provider, providerId);
	}

	private User findByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_EMAIL));
	}

	private User saveUser(OAuthUserInformation userInformation) {
		User user = User.builder()
			.email(userInformation.getEmail())
			.name(userInformation.getNickname())
			.provider(userInformation.getProvider())
			.providerId(userInformation.getProviderId())
			.profileImgUrl(userInformation.getProfileImgUrl())
			.role(Role.USER)
			.build();

		userRepository.save(user);

		return user;
	}

	@Transactional(readOnly = true)
	public LoginUserInfoRes getUserInfo(User loginUser) {
		return new LoginUserInfoRes(isAdmin(loginUser), loginUser.getProvider());
	}

	private boolean isAdmin(User loginUser) {
		return loginUser.getRole().equals(Role.ADMIN);
	}
}