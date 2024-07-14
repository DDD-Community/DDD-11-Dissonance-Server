package com.dissonance.itit.service;

import com.dissonance.itit.client.KakaoInformationFeignClient;
import com.dissonance.itit.dto.response.KakaoUserInformation;
import com.dissonance.itit.dto.response.OAuthUserInformation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@RequiredArgsConstructor
@Service
public class OauthService {
    private static final String TOKEN_PREFIX = "Bearer ";
    private final KakaoInformationFeignClient kakaoInformationFeignClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OAuthUserInformation requestUserInformation(String token) {
        log.info("Requesting user information with token: {}", token);

        // Feign Client 호출
        ResponseEntity<String> responseEntity = kakaoInformationFeignClient.call(
                "application/x-www-form-urlencoded;charset=utf-8",
                TOKEN_PREFIX + token
        );

        // 원본 JSON 응답을 로그에 출력
        String jsonResponse = responseEntity.getBody();
        log.info("Raw JSON response: {}", jsonResponse);

        // JSON 응답을 KakaoUserInformation 객체로 변환
        KakaoUserInformation userInformation = null;
        try {
            userInformation = objectMapper.readValue(jsonResponse, KakaoUserInformation.class);
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON response", e);
        }

        log.info("Parsed user information: {}", userInformation);
        return userInformation;
    }

    public String sendKakaoApiRequest(String accessToken) {
        String apiUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        log.info("Response from Kakao API: {}", response.getBody());

        return response.getBody();
    }
}