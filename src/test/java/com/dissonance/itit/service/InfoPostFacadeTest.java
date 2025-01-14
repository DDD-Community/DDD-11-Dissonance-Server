package com.dissonance.itit.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.dissonance.itit.fixture.TestFixture;
import com.dissonance.itit.image.domain.Directory;
import com.dissonance.itit.image.domain.Image;
import com.dissonance.itit.image.service.ImageService;
import com.dissonance.itit.post.domain.InfoPost;
import com.dissonance.itit.post.dto.request.InfoPostReq;
import com.dissonance.itit.post.dto.response.InfoPostCreateRes;
import com.dissonance.itit.post.dto.response.InfoPostDetailRes;
import com.dissonance.itit.post.dto.response.InfoPostUpdateRes;
import com.dissonance.itit.post.service.InfoPostFacade;
import com.dissonance.itit.post.service.InfoPostService;
import com.dissonance.itit.recruitmentPosition.service.RecruitmentPositionService;
import com.dissonance.itit.user.domain.User;

@ExtendWith(MockitoExtension.class)
public class InfoPostFacadeTest {
	@InjectMocks
	private InfoPostFacade infoPostFacade;

	@Mock
	private ImageService imageService;
	@Mock
	private InfoPostService infoPostService;
	@Mock
	private RecruitmentPositionService recruitmentPositionService;

	@Test
	@DisplayName("공고 수정 성공 (이미지 포함)")
	void updateInfoPostAndImg_withImage_returnInfoPostDetailRes() {
		// given
		Long infoPostId = 1L;
		MultipartFile imgFile = mock(MultipartFile.class);
		InfoPostReq infoPostReq = TestFixture.createInfoPostReq();
		User loginUser = TestFixture.createUser();
		Image uploadedImage = TestFixture.createImage();
		InfoPost infoPost = TestFixture.createInfoPostWithImage(uploadedImage);
		List<String> positionInfos = TestFixture.createMultiplePositionInfos();

		given(infoPostService.updateInfoPostFields(infoPostId, infoPostReq, loginUser)).willReturn(infoPost);
		given(recruitmentPositionService.findPositionInfosByInfoPostId(infoPostId)).willReturn(positionInfos);
		given(imageService.updateImage(imgFile, infoPost)).willReturn(uploadedImage);

		// when
		InfoPostDetailRes result = infoPostFacade.updateInfoPostAndImg(infoPostId, imgFile, infoPostReq, loginUser);

		// then
		assertThat(result).isNotNull();
		assertThat(infoPost.getImage()).isEqualTo(uploadedImage);
		verify(infoPostService).updateInfoPostFields(infoPostId, infoPostReq, loginUser);
		verify(recruitmentPositionService).findPositionInfosByInfoPostId(infoPostId);
		verify(imageService).updateImage(imgFile, infoPost);
	}

	@Test
	@DisplayName("공고 수정 성공 (이미지 미포함)")
	void updateInfoPostAndImg_withoutImage_returnInfoPostDetailRes() {
		// given
		Long infoPostId = 1L;
		InfoPostReq infoPostReq = TestFixture.createInfoPostReq();
		User loginUser = TestFixture.createUser();
		InfoPost infoPost = TestFixture.createInfoPostWithoutImage();
		List<String> positionInfos = TestFixture.createMultiplePositionInfos();

		given(infoPostService.updateInfoPostFields(infoPostId, infoPostReq, loginUser)).willReturn(infoPost);
		given(recruitmentPositionService.findPositionInfosByInfoPostId(infoPostId)).willReturn(positionInfos);

		// when
		InfoPostDetailRes result = infoPostFacade.updateInfoPostAndImg(infoPostId, null, infoPostReq, loginUser);

		// then
		assertThat(result).isNotNull();
		verify(infoPostService).updateInfoPostFields(infoPostId, infoPostReq, loginUser);
		verify(recruitmentPositionService).findPositionInfosByInfoPostId(infoPostId);
		verify(imageService, never()).updateImage(any(), any());
	}

	@Test
	@DisplayName("공고 생성 성공")
	void createInfoPost_returnInfoPostCreateRes() {
		// given
		MultipartFile imgFile = mock(MultipartFile.class);
		InfoPostReq infoPostReq = TestFixture.createInfoPostReq();
		User author = TestFixture.createUser();
		Image image = TestFixture.createImage();
		InfoPost infoPost = TestFixture.createInfoPostWithImage(image);

		given(imageService.upload(Directory.INFORMATION, imgFile)).willReturn(image);
		given(infoPostService.saveInfoPost(infoPostReq, author, image)).willReturn(infoPost);
		// given(infoPostReq.positionInfos()).willReturn(TestFixture.createMultiplePositionInfos());

		// when
		InfoPostCreateRes result = infoPostFacade.createInfoPost(imgFile, infoPostReq, author);

		// then
		assertThat(result).isNotNull();
		verify(imageService).upload(Directory.INFORMATION, imgFile);
		verify(infoPostService).saveInfoPost(infoPostReq, author, image);
		verify(recruitmentPositionService).addPositions(infoPost, infoPostReq.positionInfos());
	}

	@Test
	@DisplayName("공고 삭제 성공")
	void deleteInfoPostById_success() {
		// given
		Long infoPostId = 1L;
		Image image = TestFixture.createImage();
		InfoPost infoPost = TestFixture.createInfoPostWithImage(image);

		given(infoPostService.findById(infoPostId)).willReturn(infoPost);

		// when
		infoPostFacade.deleteInfoPostById(infoPostId);

		// then
		verify(infoPostService).findById(infoPostId);
		verify(imageService).delete(image);
		verify(infoPostService).deleteInfoPostById(infoPostId);
	}

	@Test
	@DisplayName("수정을 위한 공고 상세 조회 성공")
	void getInfoPostDetailByIdForUpdate_returnInfoPostUpdateRes() {
		// given
		Long infoPostId = 1L;
		InfoPostUpdateRes expectedRes = mock(InfoPostUpdateRes.class);

		given(infoPostService.getInfoPostDetailByIdForUpdate(infoPostId)).willReturn(expectedRes);

		// when
		InfoPostUpdateRes result = infoPostFacade.getInfoPostDetailByIdForUpdate(infoPostId);

		// then
		assertThat(result).isEqualTo(expectedRes);
		verify(infoPostService).getInfoPostDetailByIdForUpdate(infoPostId);
	}
}
