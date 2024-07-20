package com.dissonance.itit.service;

import com.dissonance.itit.common.jwt.util.JwtUtil;
import com.dissonance.itit.domain.Role;
import com.dissonance.itit.domain.User;
import com.dissonance.itit.dto.response.GeneratedToken;
import com.dissonance.itit.dto.response.OAuthUserInformation;
import com.dissonance.itit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final OauthService oauthService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public GeneratedToken login(String provider, String token) {
        OAuthUserInformation userInformation;

        if (provider.equals("kakao")) {
            userInformation = oauthService.requestUserInformation(token);
        } else {
            log.info("존재하지 않는 provider: " + provider);
            throw new IllegalArgumentException("존재하지 않는 provider: " + provider);
        }

        User user;

        if (userRepository.existsByProviderAndProviderId(provider, userInformation.getProviderId())) {
            log.info("[UserService] login, response: {}", userInformation);

            user = findByEmail(userInformation.getEmail());

        } else {
            log.info("[UserService] signUp, response: {}", userInformation);

            user = User.builder()
                    .email(userInformation.getEmail())
                    .password("kakao")
                    .name(userInformation.getNickname())
                    .provider(userInformation.getProvider())
                    .providerId(userInformation.getProviderId())
                    .profileImgUrl(userInformation.getProfileImgUrl())
                    .role(Role.USER)
                    .build();

            userRepository.save(user);
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole().getKey());
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 email의 사용자가 존재하지 않습니다."));
    }
}