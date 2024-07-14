package com.dissonance.itit.controller;

import com.dissonance.itit.dto.request.OauthTokenReq;
import com.dissonance.itit.service.OauthService;
import com.dissonance.itit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class OauthController {
    private final UserService userService;
    private final OauthService oauthService;

    @PostMapping("/{provider}")
    public ResponseEntity<String> getUserInfos(@PathVariable String provider,
                                               @Valid @RequestBody OauthTokenReq oauthTokenReq) {
        userService.login(provider, oauthTokenReq.accessToken());

        return ResponseEntity.ok(oauthService.sendKakaoApiRequest(oauthTokenReq.accessToken()));
    }
}
