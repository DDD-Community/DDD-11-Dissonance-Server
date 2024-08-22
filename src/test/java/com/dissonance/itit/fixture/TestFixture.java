package com.dissonance.itit.fixture;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.dissonance.itit.domain.entity.Category;
import com.dissonance.itit.domain.entity.Image;
import com.dissonance.itit.domain.entity.InfoPost;
import com.dissonance.itit.domain.entity.User;
import com.dissonance.itit.domain.enums.Role;
import com.dissonance.itit.dto.common.PositionInfo;
import com.dissonance.itit.dto.request.InfoPostReq;

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

	public static List<PositionInfo> createMultiplePositionInfos() {
		return List.of(
			new PositionInfo("개발자", 0),
			new PositionInfo("기획자", 1),
			new PositionInfo("디자이너", 2)
		);
	}

	public static List<InfoPost> createMultipleInfoPosts(User author, Image image, Category category) {
		InfoPostReq infoPostReq1 = InfoPostReq.builder()
			.title("공고 1")
			.content("내용 1")
			.organization("조직 1")
			.categoryId(4)
			.activityStartDate("2000년 1월 1일")
			.activityEndDate("2000년 1월 7일")
			.recruitmentStartDate("2000년 6월 1일")
			.recruitmentEndDate("2000년 6월 30일")
			.detailUrl("https://example.com/1")
			.build();

		InfoPostReq infoPostReq2 = InfoPostReq.builder()
			.title("공고 2")
			.content("내용 2")
			.organization("조직 2")
			.categoryId(5)
			.activityStartDate("2000년 2월 1일")
			.activityEndDate("2000년 2월 7일")
			.recruitmentStartDate("2000년 7월 1일")
			.recruitmentEndDate("2000년 7월 31일")
			.detailUrl("https://example.com/2")
			.build();

		InfoPostReq infoPostReq3 = InfoPostReq.builder()
			.title("공고 3")
			.content("내용 3")
			.organization("조직 3")
			.categoryId(6)
			.activityStartDate("2000년 3월 1일")
			.activityEndDate("2000년 3월 7일")
			.recruitmentStartDate("2000년 8월 1일")
			.recruitmentEndDate("2000년 8월 31일")
			.detailUrl("https://example.com/3")
			.build();

		InfoPost infoPost1 = createInfoPost(infoPostReq1, author, image, category);
		InfoPost infoPost2 = createInfoPost(infoPostReq2, author, image, category);
		InfoPost infoPost3 = createInfoPost(infoPostReq3, author, image, category);

		return List.of(infoPost1, infoPost2, infoPost3);
	}
}
