package com.dissonance.itit.fixture;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.dissonance.itit.domain.entity.Category;
import com.dissonance.itit.domain.entity.Image;
import com.dissonance.itit.domain.entity.InfoPost;
import com.dissonance.itit.domain.entity.User;
import com.dissonance.itit.domain.enums.Role;
import com.dissonance.itit.dto.request.InfoPostReq;
import com.dissonance.itit.dto.response.InfoPostRes;

public class TestFixture {
	public static MockMultipartFile getMockMultipartFile() {
		return new MockMultipartFile(
			"대표 썸네일 이미지",
			"thumbnail.png",
			MediaType.IMAGE_PNG_VALUE,
			"thumbnail".getBytes()
		);
	}

	public static InfoPostReq createInfoPostReq() {
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

	public static User createUser() {
		return User.builder()
			.id(1L)
			.name("김홍돌")
			.role(Role.ADMIN)
			.build();
	}

	public static User createAnotherUser() {
		return User.builder()
			.id(2L)
			.name("김홍시")
			.role(Role.USER)
			.build();
	}

	public static Image createImage() {
		return Image.builder()
			.id(5L)
			.build();
	}

	public static Category createCategory() {
		return Category.builder()
			.id(2)
			.name("해커톤")
			.build();
	}

	public static Category createAnotherCategory() {
		return Category.builder()
			.id(4)
			.name("IT 동아리")
			.build();
	}

	public static InfoPost createInfoPost(InfoPostReq infoPostReq, User author, Image image, Category category) {
		return InfoPost.builder()
			.id(1L)
			.title(infoPostReq.title())
			.content(infoPostReq.content())
			.organization(infoPostReq.organization())
			.detailUrl(infoPostReq.detailUrl())
			.image(image)
			.category(category)
			.author(author)
			.build();
	}

	public static InfoPost createInfoPostWithImage(Image image) {
		return InfoPost.builder()
			.id(1L)
			.title("Post 1")
			.image(image)
			.author(createUser())
			.category(createCategory())
			.build();
	}

	public static List<String> createMultiplePositionInfos() {
		return List.of(
			"개발자 0명",
			"기획자 1명",
			"디자이너 2명"
		);
	}

	public static List<InfoPostRes> createMultipleInfoPostRes() {
		return List.of(
			InfoPostRes.builder()
				.id(1L)
				.imgUrl("http://example.com/img1.jpg")
				.title("Post 1")
				.remainingDays("D-5")
				.build(),
			InfoPostRes.builder()
				.id(2L)
				.imgUrl("http://example.com/img2.jpg")
				.title("Post 2")
				.remainingDays("D-Day")
				.build(),
			InfoPostRes.builder()
				.id(3L)
				.imgUrl("http://example.com/img3.jpg")
				.title("Post 3")
				.remainingDays("D+3")
				.build()
		);
	}
}
