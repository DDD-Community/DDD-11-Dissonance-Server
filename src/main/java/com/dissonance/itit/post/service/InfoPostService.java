package com.dissonance.itit.post.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dissonance.itit.global.common.exception.CustomException;
import com.dissonance.itit.global.common.exception.ErrorCode;
import com.dissonance.itit.image.domain.Directory;
import com.dissonance.itit.image.domain.Image;
import com.dissonance.itit.image.service.ImageService;
import com.dissonance.itit.post.domain.Category;
import com.dissonance.itit.post.domain.InfoPost;
import com.dissonance.itit.post.dto.request.InfoPostReq;
import com.dissonance.itit.post.dto.request.InfoPostUpdateReq;
import com.dissonance.itit.post.dto.response.InfoPostCreateRes;
import com.dissonance.itit.post.dto.response.InfoPostDetailRes;
import com.dissonance.itit.post.dto.response.InfoPostDetailRes.InfoPostInfo;
import com.dissonance.itit.post.dto.response.InfoPostRes;
import com.dissonance.itit.post.dto.response.InfoPostUpdateRes;
import com.dissonance.itit.post.repository.InfoPostRepository;
import com.dissonance.itit.post.repository.InfoPostRepositorySupport;
import com.dissonance.itit.recruitmentPosition.service.RecruitmentPositionService;
import com.dissonance.itit.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InfoPostService {
	private final InfoPostRepository infoPostRepository;
	private final InfoPostRepositorySupport infoPostRepositorySupport;

	private final ImageService imageService;
	private final CategoryService categoryService;
	private final RecruitmentPositionService recruitmentPositionService;

	@Transactional
	public InfoPostCreateRes createInfoPost(MultipartFile imgFile, InfoPostReq infoPostReq, User author) {
		Image image = imageService.upload(Directory.INFORMATION, imgFile);
		Category category = categoryService.findById(infoPostReq.categoryId());

		InfoPost infoPost = infoPostRepository.save(infoPostReq.toEntity(image, author, category));

		recruitmentPositionService.addPositions(infoPost, infoPostReq.positionInfos());

		return InfoPostCreateRes.of(infoPost);
	}

	@Transactional(readOnly = true)
	public InfoPostDetailRes getInfoPostDetailById(Long infoPostId) {
		InfoPostInfo infoPostInfo = infoPostRepositorySupport.findInfoPostWithDetails(infoPostId);

		if (infoPostInfo == null) {
			throw new CustomException(ErrorCode.NON_EXISTENT_INFO_POST_ID);
		}

		List<String> positionInfos = recruitmentPositionService.findPositionInfosByInfoPostId(infoPostId);

		return InfoPostDetailRes.of(infoPostInfo, positionInfos);
	}

	@Transactional(readOnly = true)
	public InfoPost findById(Long infoPostId) {
		return infoPostRepository.findById(infoPostId)
			.orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_INFO_POST_ID));
	}

	@Transactional(readOnly = true)
	public Page<InfoPostRes> getInfoPostsByCategoryId(Integer categoryId, Pageable pageable) {
		return infoPostRepositorySupport.findInfoPostsByCategoryId(categoryId, pageable);
	}

	@Transactional
	public void deleteInfoPostById(Long infoPostId) {
		InfoPost infoPost = findById(infoPostId);
		imageService.delete(infoPost.getImage());

		infoPostRepository.deleteById(infoPostId);
	}

	public InfoPostUpdateRes getInfoPostDetailByIdForUpdate(Long infoPostId) {
		InfoPostUpdateRes.InfoPostInfo infoPostInfo = infoPostRepositorySupport.findInfoPostDetailsForUpdate(
			infoPostId);

		if (infoPostInfo == null) {
			throw new CustomException(ErrorCode.NON_EXISTENT_INFO_POST_ID);
		}

		List<String> positionInfos = recruitmentPositionService.findPositionInfosByInfoPostId(
			infoPostId);

		return InfoPostUpdateRes.of(infoPostInfo, positionInfos);
	}

	@Transactional
	public InfoPostDetailRes updateInfoPost(Long infoPostId, MultipartFile imgFile, InfoPostReq infoPostReq,
		User loginUser) {
		InfoPost infoPost = findById(infoPostId);

		validateAuthor(infoPost, loginUser);

		Category category = infoPost.getCategory().getId().equals(infoPostReq.categoryId())
			? infoPost.getCategory()
			: categoryService.findById(infoPostReq.categoryId());

		InfoPostUpdateReq updateReq = InfoPostUpdateReq.from(category, infoPostReq);
		infoPost.update(updateReq);

		recruitmentPositionService.updatePositions(infoPost, infoPostReq.positionInfos());
		List<String> positionInfos = recruitmentPositionService.findPositionInfosByInfoPostId(infoPostId);

		// TODO: S3 Transaction 처리 (데이터 정합성)
		if (imgFile != null && !imgFile.isEmpty()) {
			imageService.delete(infoPost.getImage());
			Image newImage = imageService.upload(Directory.INFORMATION, imgFile);
			infoPost.updateImage(newImage);
		}

		return InfoPostDetailRes.of(infoPost, positionInfos);
	}

	private void validateAuthor(InfoPost infoPost, User loginUser) {
		if (!infoPost.isAuthor(loginUser)) {
			throw new CustomException(ErrorCode.NO_INFO_POST_UPDATE_PERMISSION);
		}
	}

	@Transactional(readOnly = true)
	public Page<InfoPostRes> getInfoPostsByKeyword(String keyword, Pageable pageable) {
		return infoPostRepositorySupport.findInfoPostsByKeyword(keyword, pageable);
	}
}
