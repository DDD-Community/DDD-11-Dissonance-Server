package com.dissonance.itit.dto.request;

import static com.dissonance.itit.common.util.DateUtil.*;

import java.util.List;

import com.dissonance.itit.domain.entity.Category;
import com.dissonance.itit.domain.entity.Image;
import com.dissonance.itit.domain.entity.InfoPost;
import com.dissonance.itit.domain.entity.User;
import com.dissonance.itit.dto.common.PositionInfo;

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
	@Schema(description = "모집 기관 or 단체", example = "DDD")
	String organization,
	@NotBlank(message = "모집 시작일은 필수 입력입니다.")
	@Schema(description = "모집 시작 일자", example = "2024년 8월 10일")
	String recruitmentStartDate,
	@NotBlank(message = "모집 마감일은 필수 입력입니다.")
	@Schema(description = "모집 종료 일자", example = "2024년 8월 18일")
	String recruitmentEndDate,
	List<PositionInfo> positionInfos,
	@NotBlank(message = "활동 시작일은 필수 입력입니다.")
	@Schema(description = "활동 시작 일자", example = "2024년 10월 1일")
	String activityStartDate,
	@NotBlank(message = "활동 종료일은 필수 입력입니다.")
	@Schema(description = "활동 종료 일자", example = "2024년 12월 31일")
	String activityEndDate,
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
