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
import com.dissonance.itit.dto.common.PositionInfo;
import com.dissonance.itit.dto.request.InfoPostReq;
import com.dissonance.itit.dto.response.InfoPostCreateRes;
import com.dissonance.itit.dto.response.InfoPostDetailRes;
import com.dissonance.itit.dto.response.InfoPostRes;
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
		given(categoryService.findById(anyInt())).willReturn(category);
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
			"Content", "www.detailUrl.com", 100, false);

		List<PositionInfo> positionInfos = TestFixture.createMultiplePositionInfos();

		given(infoPostRepositorySupport.findById(infoPostId)).willReturn(infoPostInfo);
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
		given(infoPostRepositorySupport.findById(infoPostId)).willReturn(null);

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
			"Content", "www.detailUrl.com", 100, true);

		given(infoPostRepositorySupport.findById(infoPostId)).willReturn(reportedInfoPost);

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
}
