package com.dissonance.itit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dissonance.itit.common.exception.CustomException;
import com.dissonance.itit.common.exception.ErrorCode;
import com.dissonance.itit.domain.entity.Category;
import com.dissonance.itit.domain.entity.Image;
import com.dissonance.itit.domain.entity.InfoPost;
import com.dissonance.itit.domain.entity.User;
import com.dissonance.itit.domain.enums.Directory;
import com.dissonance.itit.dto.common.PositionInfo;
import com.dissonance.itit.dto.request.InfoPostReq;
import com.dissonance.itit.dto.response.InfoPostCreateRes;
import com.dissonance.itit.dto.response.InfoPostDetailRes;
import com.dissonance.itit.dto.response.InfoPostDetailRes.InfoPostInfo;
import com.dissonance.itit.dto.response.InfoPostRes;
import com.dissonance.itit.repository.InfoPostRepository;
import com.dissonance.itit.repository.InfoPostRepositorySupport;

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
		InfoPostInfo infoPostInfo = infoPostRepositorySupport.findById(infoPostId);

		if (infoPostInfo == null) {
			throw new CustomException(ErrorCode.NON_EXISTENT_INFO_POST_ID);
		}

		if (infoPostInfo.getReported()) {
			throw new CustomException(ErrorCode.REPORTED_INFO_POST_ID);
		}

		List<PositionInfo> positionInfos = recruitmentPositionService.findPositionInfosByInfoPostId(
			infoPostId);

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
}
