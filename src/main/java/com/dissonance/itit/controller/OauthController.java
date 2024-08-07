package com.dissonance.itit.controller;

import com.dissonance.itit.dto.request.OauthTokenReq;
import com.dissonance.itit.dto.response.GeneratedToken;
import com.dissonance.itit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("oauth")
public class OauthController {
    private final UserService userService;

    // TODO: 커스텀 어노테이션으로 로그인 유저 정보 추출
    @PostMapping("/{provider}")
    @Operation(summary = "소셜 로그인", description = "소셜 로그인 (provider - kakao, apple)")
    public ResponseEntity<GeneratedToken> getUserInfos(@PathVariable String provider,
                                               @Valid @RequestBody OauthTokenReq oauthTokenReq) {
        GeneratedToken token = userService.login(provider, oauthTokenReq.accessToken());

        return ResponseEntity.ok(token);
    }
}
