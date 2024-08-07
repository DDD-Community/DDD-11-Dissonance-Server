package com.dissonance.itit.controller;

import com.dissonance.itit.dto.response.FeaturedPostRes;
import com.dissonance.itit.service.FeaturedPostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("featured-posts")
public class FeaturedPostContorller {
    private final FeaturedPostService featuredPostService;

    @GetMapping
    @Operation(summary = "추천 게시글 조회", description = "운영진 추천 게시글 5개를 조회합니다.")
    public ResponseEntity<List<FeaturedPostRes>> getFeaturedPosts() {
        List<FeaturedPostRes> featuredPostRes = featuredPostService.getFeaturedPost();
        return ResponseEntity.ok(featuredPostRes);
    }
}
