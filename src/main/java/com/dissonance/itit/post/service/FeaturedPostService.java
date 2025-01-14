package com.dissonance.itit.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dissonance.itit.global.common.exception.CustomException;
import com.dissonance.itit.global.common.exception.ErrorCode;
import com.dissonance.itit.image.domain.Directory;
import com.dissonance.itit.image.service.ImageService;
import com.dissonance.itit.post.domain.FeaturedPost;
import com.dissonance.itit.post.domain.InfoPost;
import com.dissonance.itit.post.dto.response.FeaturedPostRes;
import com.dissonance.itit.post.repository.FeaturedPostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeaturedPostService {
	private final FeaturedPostRepository featuredPostRepository;
	private final InfoPostService infoPostService;
	private final ImageService imageService;

	@Transactional(readOnly = true)
	public List<FeaturedPostRes> getFeaturedPost() {
		List<FeaturedPost> featuredPosts = featuredPostRepository.findAll();
		return FeaturedPostRes.of(featuredPosts);
	}

	@Transactional
	public FeaturedPostRes updateFeaturedPost(Integer featuredPostId, MultipartFile imgFile, Long infoPostId) {
		FeaturedPost featuredPost = findById(featuredPostId);
		InfoPost infoPost = infoPostService.findById(infoPostId);

		// TODO: Image 객체 매핑으로 변경
		String bannerImgUrl = imageService.uploadAndGetImgPath(Directory.FEATURE, imgFile);

		featuredPost.update(infoPost, bannerImgUrl);

		return FeaturedPostRes.of(featuredPost);
	}

	@Transactional(readOnly = true)
	public FeaturedPost findById(Integer id) {
		return featuredPostRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_FEATURED_INFO_POST_ID));
	}
}
