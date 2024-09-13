package com.dissonance.itit.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dissonance.itit.common.exception.CustomException;
import com.dissonance.itit.common.exception.ErrorCode;
import com.dissonance.itit.domain.entity.Category;
import com.dissonance.itit.domain.entity.Image;
import com.dissonance.itit.domain.entity.InfoPost;
import com.dissonance.itit.domain.entity.User;
import com.dissonance.itit.dto.request.InfoPostReq;
import com.dissonance.itit.fixture.TestFixture;
import com.dissonance.itit.repository.ReportRepository;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
	@InjectMocks
	private ReportService reportService;
	@Mock
	private ReportRepository reportRepository;
	@Mock
	private InfoPostService infoPostService;

	@Test
	@DisplayName("게시글 신고")
	void reportedInfoPost_returnInfoPostId() {
		// given
		Long infoPostId = 1L;
		InfoPostReq infoPostReq = TestFixture.createInfoPostReq();
		User author = TestFixture.createUser();
		Image image = TestFixture.createImage();
		Category category = TestFixture.createCategory();
		InfoPost infoPost = TestFixture.createInfoPost(infoPostReq, author, image, category);

		given(infoPostService.findById(infoPostId)).willReturn(infoPost);
		given(reportRepository.existsByInfoPostIdAndUserId(infoPostId, author.getId()))
			.willReturn(false);

		// when
		Long result = reportService.reportedInfoPost(infoPostId, author);

		// then
		assertThat(result).isEqualTo(infoPostId);
		verify(reportRepository, times(1)).save(any());
	}

	@Test
	@DisplayName("게시글 신고시 중복 신고로 인한 exception 발생")
	void reportedInfoPost_throwCustomException_givenDuplicateReports() {
		// given
		Long infoPostId = 999L;
		User loginUser = TestFixture.createUser();
		given(reportRepository.existsByInfoPostIdAndUserId(infoPostId, loginUser.getId()))
			.willReturn(true);

		// when & then
		assertThatThrownBy(() -> reportService.reportedInfoPost(infoPostId, loginUser))
			.isInstanceOf(CustomException.class)
			.hasMessage(ErrorCode.ALREADY_REPORTED_POST.getMessage());
	}
}
