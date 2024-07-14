package com.dissonance.itit.service;

import com.dissonance.itit.domain.Role;
import com.dissonance.itit.domain.User;
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

    @Transactional
    public void login(String provider, String token) {
        OAuthUserInformation userInformation;

        if (provider.equals("kakao")) {
            userInformation = oauthService.requestUserInformation(token);
        } else {
            log.info("존재하지 않는 provider: " + provider);
            throw new IllegalArgumentException("존재하지 않는 provider: " + provider);
        }

        if (userRepository.existsByProviderAndProviderId(userInformation.getProvider(), userInformation.getProviderId())) {
            // TODO: 서버 자체 토큰 발급하여 Response 전달
            log.info("[UserService] login, response: {}", userInformation);
        } else {
            log.info("[UserService] signUp, response: {}", userInformation);

            User user = User.builder()
                    .email(userInformation.getEmail())
                    .name(userInformation.getNickname())
                    .provider(userInformation.getProvider())
                    .providerId(userInformation.getProviderId())
                    .profileImgUrl(userInformation.getProfileImgUrl())
                    .role(Role.USER)
                    .build();

            userRepository.save(user);
        }
    }
}