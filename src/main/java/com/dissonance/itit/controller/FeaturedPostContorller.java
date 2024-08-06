package com.dissonance.itit.controller;

import com.dissonance.itit.dto.response.FeaturedPostRes;
import com.dissonance.itit.service.FeaturedPostService;
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
    public ResponseEntity<List<FeaturedPostRes>> getFeaturedPosts() {
        List<FeaturedPostRes> featuredPostRes = featuredPostService.getFeaturedPost();
        return ResponseEntity.ok(featuredPostRes);
    }
}
