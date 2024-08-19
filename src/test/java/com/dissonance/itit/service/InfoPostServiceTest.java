package com.dissonance.itit.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.dissonance.itit.domain.entity.Category;
import com.dissonance.itit.domain.entity.Image;
import com.dissonance.itit.domain.entity.InfoPost;
import com.dissonance.itit.domain.entity.User;
import com.dissonance.itit.domain.enums.Directory;
import com.dissonance.itit.domain.enums.Role;
import com.dissonance.itit.dto.request.InfoPostReq;
import com.dissonance.itit.dto.response.InfoPostCreateRes;
import com.dissonance.itit.repository.InfoPostRepository;

@ExtendWith(MockitoExtension.class)
public class InfoPostServiceTest {
	@InjectMocks
	private InfoPostService infoPostService;

	@Mock
	private InfoPostRepository infoPostRepository;
	@Mock
	private ImageService imageService;
	@Mock
	private CategoryService categoryService;
	@Mock
	private RecruitmentPositionService recruitmentPositionService;

	@Test
	@DisplayName("공고를 생성한다.")
	public void createInfoPost_returnInfoPostCreateRes() {
	    // given
		MultipartFile imgFile = getMockMultipartFile();
		InfoPostReq infoPostReq = createInfoPostReq();
		User author = createUser();
		Image image = createImage();
		Category category = createCategory();
		InfoPost infoPost = createInfoPost(infoPostReq, author, image, category);
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
		verify(infoPostRepository).save(any());
		verify(recruitmentPositionService).addPositions(infoPost, infoPostReq.positionInfos());
	}


	// TODO: test fixture class 분리
	private static MockMultipartFile getMockMultipartFile() {
		return new MockMultipartFile(
			"대표 썸네일 이미지",
			"thumbnail.png",
			MediaType.IMAGE_PNG_VALUE,
			"thumbnail".getBytes()
		);
	}

	private InfoPostReq createInfoPostReq() {
		return InfoPostReq.builder()
			.title("공고공고")
			.content("내용내용")
			.organization("ddd")
			.categoryId(4)
			.activityStartDate("2000년 2월 2일")
			.activityEndDate("2000년 2월 9일")
			.recruitmentStartDate("2000년 8월 1일")
			.recruitmentEndDate("2000년 12월 31일")
			.detailUrl("https://www.naver.com/")
			.build();
	}

	private User createUser() {
		return User.builder()
			.id(1L)
			.name("김홍돌")
			.role(Role.ADMIN)
			.build();
	}

	private Image createImage() {
		return Image.builder()
			.id(5L)
			.build();
	}

	private Category createCategory() {
		return Category.builder()
			.id(2)
			.name("해커톤")
			.build();
	}

	private InfoPost createInfoPost(InfoPostReq infoPostReq, User author, Image image, Category category) {
		return InfoPost.builder()
			.title(infoPostReq.title())
			.content(infoPostReq.content())
			.organization(infoPostReq.organization())
			.detailUrl(infoPostReq.detailUrl())
			.image(image)
			.category(category)
			.author(author)
			.build();
	}
}
