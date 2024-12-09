package com.dissonance.itit.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dissonance.itit.post.domain.FeaturedPost;
import com.dissonance.itit.post.dto.response.FeaturedPostRes;
import com.dissonance.itit.post.repository.FeaturedPostRepository;

import lombok.RequiredArgsConstructor;

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
