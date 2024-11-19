package com.dissonance.itit.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import com.dissonance.itit.common.exception.CustomException;
import com.dissonance.itit.common.exception.ErrorCode;
import com.dissonance.itit.domain.entity.Category;
import com.dissonance.itit.domain.entity.Image;
import com.dissonance.itit.domain.entity.InfoPost;
import com.dissonance.itit.domain.entity.User;
import com.dissonance.itit.domain.enums.Directory;
import com.dissonance.itit.dto.request.InfoPostReq;
import com.dissonance.itit.dto.response.InfoPostCreateRes;
import com.dissonance.itit.dto.response.InfoPostDetailRes;
import com.dissonance.itit.dto.response.InfoPostRes;
import com.dissonance.itit.dto.response.InfoPostUpdateRes;
import com.dissonance.itit.fixture.TestFixture;
import com.dissonance.itit.repository.InfoPostRepository;
import com.dissonance.itit.repository.InfoPostRepositorySupport;

@ExtendWith(MockitoExtension.class)
public class InfoPostServiceTest {
	@InjectMocks
	private InfoPostService infoPostService;

	@Mock
	private InfoPostRepository infoPostRepository;
	@Mock
	private InfoPostRepositorySupport infoPostRepositorySupport;
	@Mock
	private ImageService imageService;
	@Mock
	private CategoryService categoryService;
	@Mock
	private RecruitmentPositionService recruitmentPositionService;

	@Test
	@DisplayName("공고 생성 성공")
	public void createInfoPost_returnInfoPostCreateRes() {
		// given
		MockMultipartFile imgFile = TestFixture.getMockMultipartFile();
		InfoPostReq infoPostReq = TestFixture.createInfoPostReq();
		User author = TestFixture.createUser();
		Image image = TestFixture.createImage();
		Category category = TestFixture.createCategory();
		InfoPost infoPost = TestFixture.createInfoPost(infoPostReq, author, image, category);
		InfoPostCreateRes expectedResponse = InfoPostCreateRes.of(infoPost);

		given(imageService.upload(Directory.INFORMATION, imgFile)).willReturn(image);
		given(categoryService.findById(anyInt())).willReturn(TestFixture.createAnotherCategory());
		given(infoPostRepository.save(any())).willReturn(infoPost);

		// When
		InfoPostCreateRes actualResponse = infoPostService.createInfoPost(imgFile, infoPostReq, author);

		// Then
		assertThat(actualResponse).isEqualTo(expectedResponse);
		verify(imageService).upload(Directory.INFORMATION, imgFile);
		verify(categoryService).findById(infoPostReq.categoryId());
		verify(infoPostRepository).save(any(InfoPost.class));
		verify(recruitmentPositionService).addPositions(infoPost, infoPostReq.positionInfos());
	}

	@Test
	@DisplayName("공고 상세 조회 성공")
	void getInfoPostDetailById_returnInfoPostDetailRes() {
		// given
		Long infoPostId = 1L;
		InfoPostDetailRes.InfoPostInfo infoPostInfo = new InfoPostDetailRes.InfoPostInfo(
			"Title", "Category", "Organization",
			LocalDate.now(), LocalDate.now().plusDays(5),
			LocalDate.now(), LocalDate.now().plusMonths(1),
			"Content", "www.detailUrl.com", 100, false, "www.imageUrl.com");

		List<String> positionInfos = TestFixture.createMultiplePositionInfos();

		given(infoPostRepositorySupport.findInfoPostWithDetails(infoPostId)).willReturn(infoPostInfo);
		given(recruitmentPositionService.findPositionInfosByInfoPostId(infoPostId)).willReturn(positionInfos);

		// when
		InfoPostDetailRes result = infoPostService.getInfoPostDetailById(infoPostId);

		// then
		assertThat(result.getTitle()).isEqualTo(infoPostInfo.getTitle());
		assertThat(result.getContent()).isEqualTo(infoPostInfo.getContent());
		assertThat(result.getPositionInfos()).isEqualTo(positionInfos);
	}

	@Test
	@DisplayName("공고 상세 조회시 존재하지 않는 ID로 조회하여 exception 발생")
	void getInfoPostDetailById_throwCustomException_givenNonExistentId() {
		// given
		Long infoPostId = 999L;
		given(infoPostRepositorySupport.findInfoPostWithDetails(infoPostId)).willReturn(null);

		// when & then
		assertThatThrownBy(() -> infoPostService.getInfoPostDetailById(infoPostId))
			.isInstanceOf(CustomException.class)
			.hasMessage(ErrorCode.NON_EXISTENT_INFO_POST_ID.getMessage());
	}

	@Test
	@DisplayName("공고 상세 조회시 신고된 않는 ID로 조회하여 exception 발생")
	void getInfoPostDetailById_throwCustomException_givenReportedInfoPostId() {
		// given
		Long infoPostId = 1L;
		InfoPostDetailRes.InfoPostInfo reportedInfoPost = new InfoPostDetailRes.InfoPostInfo(
			"Title", "Category", "Organization",
			LocalDate.now(), LocalDate.now().plusDays(5),
			LocalDate.now(), LocalDate.now().plusMonths(1),
			"Content", "www.detailUrl.com", 100, true, "www.imageUrl.com");

		given(infoPostRepositorySupport.findInfoPostWithDetails(infoPostId)).willReturn(reportedInfoPost);

		// when & then
		assertThatThrownBy(() -> infoPostService.getInfoPostDetailById(infoPostId))
			.isInstanceOf(CustomException.class)
			.hasMessage(ErrorCode.REPORTED_INFO_POST_ID.getMessage());
	}

	@Test
	@DisplayName("공고 게시글 목록 page 조회")
	void getInfoPostsByCategoryId_returnInfoPostResPage() {
		// Given
		Integer categoryId = 1;
		Pageable pageable = PageRequest.of(0, 10);
		List<InfoPostRes> postResList = TestFixture.createMultipleInfoPostRes();
		Page<InfoPostRes> expectedPage = new PageImpl<>(postResList, pageable, postResList.size());

		given(infoPostRepositorySupport.findInfoPostsByCategoryId(categoryId, pageable)).willReturn(expectedPage);

		// When
		Page<InfoPostRes> result = infoPostService.getInfoPostsByCategoryId(categoryId, pageable);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(3);

		List<InfoPostRes> content = result.getContent();
		assertAll(
			() -> assertThat(content.get(0).getRemainingDays()).isEqualTo("D-5"),
			() -> assertThat(content.get(1).getRemainingDays()).isEqualTo("D-Day"),
			() -> assertThat(content.get(2).getRemainingDays()).isEqualTo("D+3")
		);
	}

	@Test
	@DisplayName("존재하지 않는 ID로 조회하여 exception 발생")
	void findById_throwCustomException_givenNonExistentId() {
		// given
		Long infoPostId = 999L;
		given(infoPostRepository.findById(infoPostId)).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> infoPostService.findById(infoPostId))
			.isInstanceOf(CustomException.class)
			.hasMessage(ErrorCode.NON_EXISTENT_INFO_POST_ID.getMessage());
	}

	@Test
	@DisplayName("InfoPost 삭제 성공")
	void deleteInfoPostById_success() {
		// given
		Long infoPostId = 1L;
		Image image = TestFixture.createImage();
		InfoPost infoPost = TestFixture.createInfoPostWithImage(image);

		given(infoPostRepository.findById(infoPostId)).willReturn(Optional.of(infoPost));

		// when
		infoPostService.deleteInfoPostById(infoPostId);

		// then
		verify(imageService).delete(infoPost.getImage());
		verify(infoPostRepository).deleteById(infoPostId);
	}

	@Test
	@DisplayName("존재하지 않는 ID로 InfoPost 삭제 시도시 CustomException 발생")
	void deleteInfoPostById_throwCustomException_givenNonExistentId() {
		// given
		Long infoPostId = 999L;
		given(infoPostRepository.findById(infoPostId)).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> infoPostService.deleteInfoPostById(infoPostId))
			.isInstanceOf(CustomException.class)
			.hasMessage(ErrorCode.NON_EXISTENT_INFO_POST_ID.getMessage());
	}

	@Test
	@DisplayName("수정을 위한 공고 상세 조회 성공")
	void getInfoPostDetailByIdForUpdate_returnInfoPostUpdateRes() {
		// given
		Long infoPostId = 1L;
		InfoPostUpdateRes.InfoPostInfo infoPostInfo = new InfoPostUpdateRes.InfoPostInfo(
			"Title", 2, "Organization",
			LocalDate.now(), LocalDate.now().plusDays(5),
			LocalDate.now(), LocalDate.now().plusMonths(1),
			"Content", "www.detailUrl.com", "www.imgUrl.com");

		List<String> positionInfos = TestFixture.createMultiplePositionInfos();

		given(infoPostRepositorySupport.findInfoPostDetailsForUpdate(infoPostId)).willReturn(infoPostInfo);
		given(recruitmentPositionService.findPositionInfosByInfoPostId(infoPostId)).willReturn(positionInfos);

		// when
		InfoPostUpdateRes result = infoPostService.getInfoPostDetailByIdForUpdate(infoPostId);

		// then
		assertThat(result.getTitle()).isEqualTo(infoPostInfo.getTitle());
		assertThat(result.getContent()).isEqualTo(infoPostInfo.getContent());
		assertThat(result.getPositionInfos()).isEqualTo(positionInfos);
	}

	@Test
	@DisplayName("수정을 위한 공고 상세 조회시 존재하지 않는 ID로 조회하여 exception 발생")
	void getInfoPostDetailByIdForUpdate_throwCustomException_givenNonExistentId() {
		// given
		Long infoPostId = 999L;
		given(infoPostRepositorySupport.findInfoPostDetailsForUpdate(infoPostId)).willReturn(null);

		// when & then
		assertThatThrownBy(() -> infoPostService.getInfoPostDetailByIdForUpdate(infoPostId))
			.isInstanceOf(CustomException.class)
			.hasMessage(ErrorCode.NON_EXISTENT_INFO_POST_ID.getMessage());
	}

	@Test
	@DisplayName("공고 수정 성공 - 이미지 변경 있음")
	void updateInfoPost_withNewImage_returnInfoPostDetailRes() {
		// given
		Long infoPostId = 1L;
		MockMultipartFile imgFile = TestFixture.getMockMultipartFile();
		InfoPostReq infoPostReq = TestFixture.createInfoPostReq();
		User loginUser = TestFixture.createUser();
		Image oldImage = TestFixture.createImage();
		Image newImage = TestFixture.createImage();
		Category newCategory = TestFixture.createCategory();
		InfoPost infoPost = TestFixture.createInfoPostWithImage(oldImage);

		given(infoPostRepository.findById(infoPostId)).willReturn(Optional.of(infoPost));
		given(categoryService.findById(infoPostReq.categoryId())).willReturn(newCategory);
		given(imageService.upload(Directory.INFORMATION, imgFile)).willReturn(newImage);
		given(recruitmentPositionService.findPositionInfosByInfoPostId(infoPostId))
			.willReturn(TestFixture.createMultiplePositionInfos());

		// when
		InfoPostDetailRes result = infoPostService.updateInfoPost(infoPostId, imgFile, infoPostReq, loginUser);

		// then
		assertThat(result).isNotNull();
		verify(imageService).delete(oldImage);
		verify(imageService).upload(Directory.INFORMATION, imgFile);
		verify(recruitmentPositionService).updatePositions(infoPost, infoPostReq.positionInfos());
	}

	@Test
	@DisplayName("공고 수정 성공 - 이미지 변경 없음")
	void updateInfoPost_withoutNewImage_returnInfoPostDetailRes() {
		// given
		Long infoPostId = 1L;
		InfoPostReq infoPostReq = TestFixture.createInfoPostReq();
		User loginUser = TestFixture.createUser();
		Category sameCategory = TestFixture.createCategory();
		InfoPost infoPost = TestFixture.createInfoPost(infoPostReq, loginUser, TestFixture.createImage(), sameCategory);

		given(infoPostRepository.findById(infoPostId)).willReturn(Optional.of(infoPost));
		given(categoryService.findById(any())).willReturn(TestFixture.createCategory());
		given(recruitmentPositionService.findPositionInfosByInfoPostId(infoPostId))
			.willReturn(TestFixture.createMultiplePositionInfos());

		// when
		InfoPostDetailRes result = infoPostService.updateInfoPost(infoPostId, null, infoPostReq, loginUser);

		// then
		assertThat(result).isNotNull();
		verify(imageService, never()).delete(any());
		verify(imageService, never()).upload(any(), any());
		verify(recruitmentPositionService).updatePositions(infoPost, infoPostReq.positionInfos());
	}

	@Test
	@DisplayName("권한 없는 사용자의 공고 수정 시도시 exception 발생")
	void updateInfoPost_throwCustomException_givenUnauthorizedUser() {
		// given
		Long infoPostId = 1L;
		InfoPostReq infoPostReq = TestFixture.createInfoPostReq();
		User author = TestFixture.createUser();
		User unauthorizedUser = TestFixture.createAnotherUser();
		InfoPost infoPost = TestFixture.createInfoPost(infoPostReq, author, TestFixture.createImage(),
			TestFixture.createCategory());

		given(infoPostRepository.findById(infoPostId)).willReturn(Optional.of(infoPost));

		// when & then
		assertThatThrownBy(() -> infoPostService.updateInfoPost(infoPostId, null, infoPostReq, unauthorizedUser))
			.isInstanceOf(CustomException.class)
			.hasMessage(ErrorCode.NO_INFO_POST_UPDATE_PERMISSION.getMessage());
	}
}
