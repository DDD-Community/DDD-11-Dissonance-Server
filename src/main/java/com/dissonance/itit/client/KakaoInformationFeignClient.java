package com.dissonance.itit.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KakaoInformationFeignClient",
        url = "${kakao.api_url.information}")
public interface KakaoInformationFeignClient {
    @GetMapping
    ResponseEntity<String> call(
            @RequestHeader("Content-Type") String contentType,
            @RequestHeader("Authorization") String accessToken
    );
}
