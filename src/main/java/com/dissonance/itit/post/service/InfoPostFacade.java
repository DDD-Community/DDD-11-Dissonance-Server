package com.dissonance.itit.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dissonance.itit.global.common.exception.CustomException;
import com.dissonance.itit.global.common.exception.ErrorCode;
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
		// 1. DB 작업: 게시글 필드 업데이트
		InfoPost infoPost = infoPostService.updateInfoPostFields(infoPostId, infoPostReq, loginUser);
		List<String> positionInfos = recruitmentPositionService.findPositionInfosByInfoPostId(infoPostId);

		// 2. 이미지 처리: S3 작업은 트랜잭션 외부에서 관리
		if (imgFile != null) {
			try {
				Image oldImage = infoPost.getImage();
				Image newImage = imageService.upload(Directory.INFORMATION, imgFile);

				// DB에 이미지 정보 반영
				infoPost.updateImage(newImage);

				// 이전 이미지 삭제는 DB 업데이트 후 처리
				imageService.delete(oldImage);
			} catch (Exception e) {
				throw new CustomException(ErrorCode.IMAGE_UPDATE_FAILED);
			}
		}

		return InfoPostDetailRes.of(infoPost, positionInfos);
	}

	public InfoPostCreateRes createInfoPost(MultipartFile imgFile, InfoPostReq infoPostReq, User author) {
		// 이미지 업로드
		Image image = imageService.upload(Directory.INFORMATION, imgFile);

		try {
			// DB에 게시글 저장
			InfoPost infoPost = infoPostService.saveInfoPost(infoPostReq, author, image);
			recruitmentPositionService.addPositions(infoPost, infoPostReq.positionInfos());

			return InfoPostCreateRes.of(infoPost);
		} catch (Exception ex) {
			imageService.delete(image); // 공고 업로드 중 DB 오류 발생 시, 업로드된 이미지 삭제

			throw new CustomException(ErrorCode.POST_CREATION_FAILED);
		}
	}

	@Transactional
	public void deleteInfoPostById(Long infoPostId) {
		InfoPost infoPost = infoPostService.findById(infoPostId);

		// 1. DB 데이터 먼저 삭제
		infoPostService.deleteInfoPostById(infoPostId);

		// 2. DB 삭제 성공 후 이미지 삭제
		imageService.delete(infoPost.getImage());
	}

	@Transactional(readOnly = true)
	public InfoPostUpdateRes getInfoPostDetailByIdForUpdate(Long infoPostId) {
		return infoPostService.getInfoPostDetailByIdForUpdate(infoPostId);
	}
}
