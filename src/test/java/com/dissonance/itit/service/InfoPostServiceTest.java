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

import com.dissonance.itit.bookmark.repository.BookmarkRepository;
import com.dissonance.itit.fixture.TestFixture;
import com.dissonance.itit.global.common.exception.CustomException;
import com.dissonance.itit.global.common.exception.ErrorCode;
import com.dissonance.itit.image.domain.Image;
import com.dissonance.itit.post.domain.Category;
import com.dissonance.itit.post.domain.InfoPost;
import com.dissonance.itit.post.dto.request.InfoPostReq;
import com.dissonance.itit.post.dto.response.InfoPostDetailRes;
import com.dissonance.itit.post.dto.response.InfoPostRes;
import com.dissonance.itit.post.dto.response.InfoPostUpdateRes;
import com.dissonance.itit.post.repository.InfoPostRepository;
import com.dissonance.itit.post.repository.InfoPostRepositorySupport;
import com.dissonance.itit.post.service.CategoryService;
import com.dissonance.itit.post.service.InfoPostService;
import com.dissonance.itit.recruitmentPosition.service.RecruitmentPositionService;
import com.dissonance.itit.user.domain.User;

@ExtendWith(MockitoExtension.class)
public class InfoPostServiceTest {
	@InjectMocks
	private InfoPostService infoPostService;

	@Mock
	private InfoPostRepository infoPostRepository;
	@Mock
	private InfoPostRepositorySupport infoPostRepositorySupport;
	@Mock
	private CategoryService categoryService;
	@Mock
	private RecruitmentPositionService recruitmentPositionService;
	@Mock
	private BookmarkRepository bookmarkRepository;

	@Test
	@DisplayName("공고 생성 성공")
	public void createInfoPost_returnInfoPostCreateRes() {
		// given
		InfoPostReq infoPostReq = TestFixture.createInfoPostReq();
		User author = TestFixture.createUser();
		Image image = TestFixture.createImage();
		Category category = TestFixture.createCategory();
		InfoPost infoPost = TestFixture.createInfoPost(infoPostReq, author, image, category);

		given(categoryService.findById(anyInt())).willReturn(TestFixture.createAnotherCategory());
		given(infoPostRepository.save(any())).willReturn(infoPost);

		// When
		InfoPost actualResponse = infoPostService.saveInfoPost(infoPostReq, author, image);

		// Then
		assertThat(actualResponse).isEqualTo(infoPost);
		verify(categoryService).findById(infoPostReq.categoryId());
		verify(infoPostRepository).save(any(InfoPost.class));
	}

	@Test
	@DisplayName("공고 상세 조회 성공 - 로그인 X")
	void getInfoPostDetailById_returnInfoPostDetailRes_WhenNonUser() {
		// given
		Long infoPostId = 1L;
		InfoPostDetailRes.InfoPostInfo infoPostInfo = new InfoPostDetailRes.InfoPostInfo(
			"Title", "Category", "Organization",
			LocalDate.now(), LocalDate.now().plusDays(5),
			LocalDate.now(), LocalDate.now().plusMonths(1),
			"Content", "www.detailUrl.com", 100, "www.imageUrl.com");

		List<String> positionInfos = TestFixture.createMultiplePositionInfos();

		given(infoPostRepositorySupport.findInfoPostWithDetails(infoPostId)).willReturn(infoPostInfo);
		given(recruitmentPositionService.findPositionInfosByInfoPostId(infoPostId)).willReturn(positionInfos);

		// when
		InfoPostDetailRes result = infoPostService.getInfoPostDetailById(infoPostId, null);

		// then
		assertThat(result.getTitle()).isEqualTo(infoPostInfo.getTitle());
		assertThat(result.getContent()).isEqualTo(infoPostInfo.getContent());
		assertThat(result.getPositionInfos()).isEqualTo(positionInfos);
		assertThat(result.getIsBookmarked()).isFalse();
		verifyNoInteractions(bookmarkRepository);
	}

	@Test
	@DisplayName("공고 상세 조회 성공 - 로그인 O")
	void getInfoPostDetailById_returnInfoPostDetailRes_WhenUser() {
		// given
		Long infoPostId = 1L;
		Long userId = 223L;
		InfoPostDetailRes.InfoPostInfo infoPostInfo = new InfoPostDetailRes.InfoPostInfo(
			"Title", "Category", "Organization",
			LocalDate.now(), LocalDate.now().plusDays(5),
			LocalDate.now(), LocalDate.now().plusMonths(1),
			"Content", "www.detailUrl.com", 100, "www.imageUrl.com");

		List<String> positionInfos = TestFixture.createMultiplePositionInfos();

		given(infoPostRepositorySupport.findInfoPostWithDetails(infoPostId)).willReturn(infoPostInfo);
		given(recruitmentPositionService.findPositionInfosByInfoPostId(infoPostId)).willReturn(positionInfos);
		given(bookmarkRepository.existsByUserIdAndPostId(infoPostId, userId)).willReturn(true);

		// when
		InfoPostDetailRes result = infoPostService.getInfoPostDetailById(infoPostId, userId);

		// then
		assertThat(result.getTitle()).isEqualTo(infoPostInfo.getTitle());
		assertThat(result.getContent()).isEqualTo(infoPostInfo.getContent());
		assertThat(result.getPositionInfos()).isEqualTo(positionInfos);
		assertThat(result.getIsBookmarked()).isTrue();
		verify(bookmarkRepository).existsByUserIdAndPostId(infoPostId, userId);
	}

	@Test
	@DisplayName("공고 상세 조회시 존재하지 않는 ID로 조회하여 exception 발생")
	void getInfoPostDetailById_throwCustomException_givenNonExistentId() {
		// given
		Long infoPostId = 999L;
		given(infoPostRepositorySupport.findInfoPostWithDetails(infoPostId)).willReturn(null);

		// when & then
		assertThatThrownBy(() -> infoPostService.getInfoPostDetailById(infoPostId, null))
			.isInstanceOf(CustomException.class)
			.hasMessage(ErrorCode.NON_EXISTENT_INFO_POST_ID.getMessage());
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

		// when
		infoPostService.deleteInfoPostById(infoPostId);

		// then
		verify(infoPostRepository).deleteById(infoPostId);
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
	@DisplayName("공고 수정 성공")
	void updateInfoPost_withNewImage_returnInfoPostDetailRes() {
		// given
		Long infoPostId = 1L;
		InfoPostReq infoPostReq = TestFixture.createInfoPostReq();
		User loginUser = TestFixture.createUser();
		Image image = TestFixture.createImage();
		Category newCategory = TestFixture.createCategory();
		InfoPost infoPost = TestFixture.createInfoPostWithImage(image);

		given(infoPostRepository.findById(infoPostId)).willReturn(Optional.of(infoPost));
		given(categoryService.findById(infoPostReq.categoryId())).willReturn(newCategory);

		// when
		InfoPost result = infoPostService.updateInfoPostFields(infoPostId, infoPostReq, loginUser);

		// then
		assertThat(result).isNotNull();
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
		assertThatThrownBy(() -> infoPostService.updateInfoPostFields(infoPostId, infoPostReq, unauthorizedUser))
			.isInstanceOf(CustomException.class)
			.hasMessage(ErrorCode.NO_INFO_POST_UPDATE_PERMISSION.getMessage());
	}
}
