package com.dissonance.itit.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dissonance.itit.image.domain.Directory;
import com.dissonance.itit.image.domain.Image;
import com.dissonance.itit.image.service.ImageService;
import com.dissonance.itit.post.domain.InfoPost;
import com.dissonance.itit.post.dto.request.InfoPostReq;
import com.dissonance.itit.post.dto.response.InfoPostCreateRes;
import com.dissonance.itit.post.dto.response.InfoPostDetailRes;
import com.dissonance.itit.post.dto.response.InfoPostUpdateRes;
import com.dissonance.itit.recruitmentPosition.service.RecruitmentPositionService;
import com.dissonance.itit.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InfoPostFacade {
	private final ImageService imageService;
	private final InfoPostService infoPostService;
	private final RecruitmentPositionService recruitmentPositionService;

	@Transactional
	public InfoPostDetailRes updateInfoPostAndImg(Long infoPostId, MultipartFile imgFile, InfoPostReq infoPostReq,
		User loginUser) {
		InfoPost infoPost = infoPostService.updateInfoPostFields(infoPostId, infoPostReq, loginUser);
		List<String> positionInfos = recruitmentPositionService.findPositionInfosByInfoPostId(infoPostId);

		if (imgFile != null) {
			Image uploadedImage = imageService.updateImage(imgFile, infoPost);
			infoPost.updateImage(uploadedImage);
		}

		return InfoPostDetailRes.of(infoPost, positionInfos);
	}

	@Transactional
	public InfoPostCreateRes createInfoPost(MultipartFile imgFile, InfoPostReq infoPostReq, User author) {
		Image image = imageService.upload(Directory.INFORMATION, imgFile);

		InfoPost infoPost = infoPostService.saveInfoPost(infoPostReq, author, image);

		recruitmentPositionService.addPositions(infoPost, infoPostReq.positionInfos());

		return InfoPostCreateRes.of(infoPost);
	}

	@Transactional
	public void deleteInfoPostById(Long infoPostId) {
		InfoPost infoPost = infoPostService.findById(infoPostId);

		imageService.delete(infoPost.getImage());

		infoPostService.deleteInfoPostById(infoPostId);
	}

	@Transactional(readOnly = true)
	public InfoPostUpdateRes getInfoPostDetailByIdForUpdate(Long infoPostId) {
		return infoPostService.getInfoPostDetailByIdForUpdate(infoPostId);
	}
}
