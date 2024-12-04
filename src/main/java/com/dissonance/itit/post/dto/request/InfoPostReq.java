package com.dissonance.itit.post.dto.request;

import static com.dissonance.itit.global.common.util.DateUtil.*;

import java.util.List;

import com.dissonance.itit.image.domain.Image;
import com.dissonance.itit.post.domain.Category;
import com.dissonance.itit.post.domain.InfoPost;
import com.dissonance.itit.user.domain.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record InfoPostReq(
	@NotBlank(message = "제목은 필수 입력입니다.")
	@Schema(description = "제목", example = "공모전1")
	String title,
	@NotNull(message = "카테고리 id는 필수 입력입니다.")
	@Schema(description = "공고 카테고리 id", example = "3")
	Integer categoryId,
	@NotBlank(message = "모집 기관, 단체는 필수 입력입니다.")
	@Schema(description = "모집 기관 or 단체", example = "DDD")
	String organization,
	@Schema(description = "모집 시작 일자", example = "2024년 8월 10일")
	String recruitmentStartDate,
	@Schema(description = "모집 종료 일자", example = "2024년 8월 18일")
	String recruitmentEndDate,
	List<String> positionInfos,
	@Schema(description = "활동 시작 일자", example = "2024년 10월 1일")
	String activityStartDate,
	@Schema(description = "활동 종료 일자", example = "2024년 12월 31일")
	String activityEndDate,
	@NotBlank(message = "활동 내용은 필수 입력입니다.")
	@Schema(description = "활동 내용", example = "여러분의 창의력과 디자인 역량을 발휘해볼 특별한 기회를 놓치지 마세요")
	String content,
	@NotBlank(message = "공고 url은 필수 입력입니다.")
	@Schema(description = "공고 url", example = "https://www.google.com/")
	String detailUrl
) {
	public InfoPost toEntity(Image image, User author, Category category) {
		return InfoPost.builder()
			.image(image)
			.author(author)
			.category(category)
			.title(title())
			.organization(organization())
			.content(content())
			.viewCount(0)
			.recruitmentStartDate(stringToDate(recruitmentStartDate()))
			.recruitmentEndDate(stringToDate(recruitmentEndDate()))
			.activityStartDate(stringToDate(activityStartDate()))
			.activityEndDate(stringToDate(activityEndDate()))
			.detailUrl(detailUrl())
			.reported(false)
			.recruitmentClosed(false)
			.build();
	}
}
