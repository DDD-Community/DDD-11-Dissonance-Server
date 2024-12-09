package com.dissonance.itit.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dissonance.itit.global.common.exception.CustomException;
import com.dissonance.itit.global.common.exception.ErrorCode;
import com.dissonance.itit.global.common.redis.RedisService;
import com.dissonance.itit.global.common.util.JwtUtil;
import com.dissonance.itit.global.security.dto.GeneratedToken;
import com.dissonance.itit.oauth.dto.OAuthUserInformation;
import com.dissonance.itit.oauth.enums.SocialLoginProvider;
import com.dissonance.itit.oauth.service.OAuthService;
import com.dissonance.itit.oauth.service.OAuthServiceFactory;
import com.dissonance.itit.user.domain.Role;
import com.dissonance.itit.user.domain.User;
import com.dissonance.itit.user.dto.LoginUserInfoRes;
import com.dissonance.itit.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
	private final JwtUtil jwtUtil;
	private final RedisService redisService;
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

	public GeneratedToken accessTokenByRefreshToken(String accessTokenHeader, String refreshToken) {
		String accessToken = jwtUtil.resolveToken(accessTokenHeader);

		String uid = jwtUtil.getUid(accessToken);
		String data = redisService.getValues(uid);

		if (data == null || !data.equals(refreshToken)) {
			log.info("Invalid Token");
			throw new CustomException(ErrorCode.UNAUTHORIZED_REFRESH_TOKEN);
		}
		String role = jwtUtil.getRole(accessToken);

		return jwtUtil.generateToken(uid, role);
	}

	@Transactional
	public void logout(String accessTokenHeader) {
		String accessToken = jwtUtil.resolveToken(accessTokenHeader);

		jwtUtil.verifyToken(accessToken);

		String uid = jwtUtil.getUid(accessToken);
		long time = jwtUtil.getExpiration(accessToken);

		redisService.setValuesWithTimeout(accessToken, "logout", time);
		redisService.deleteValues(uid);
	}

	@Transactional
	public void withdraw(Long loginUserId) {
		userRepository.deleteById(loginUserId);
	}
}