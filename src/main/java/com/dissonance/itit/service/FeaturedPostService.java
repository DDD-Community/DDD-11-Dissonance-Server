package com.dissonance.itit.service;

import com.dissonance.itit.domain.entity.FeaturedPost;
import com.dissonance.itit.dto.response.FeaturedPostRes;
import com.dissonance.itit.repository.FeaturedPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeaturedPostService {
    private final FeaturedPostRepository featuredPostRepository;

    @Transactional(readOnly = true)
    public List<FeaturedPostRes> getFeaturedPost() {
        List<FeaturedPost> featuredPosts = featuredPostRepository.findAll();
        return FeaturedPostRes.of(featuredPosts);
    }
}
