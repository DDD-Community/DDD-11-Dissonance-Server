package com.dissonance.itit.dto.response;

import static com.dissonance.itit.common.util.DateUtil.*;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InfoPostUpdateRes {
	@Schema(description = "제목", example = "공모전1")
	private String title;
	@Schema(description = "공고 카테고리 id", example = "3")
	private Integer categoryId;
	@Schema(description = "모집 기관 or 단체", example = "DDD")
	private String organization;
	@Schema(description = "모집 시작 일자", example = "2024년 8월 10일")
	private String recruitmentStartDate;
	@Schema(description = "모집 종료 일자", example = "2024년 8월 18일")
	private String recruitmentEndDate;
	private List<String> positionInfos;
	@Schema(description = "활동 시작 일자", example = "2024년 10월 1일")
	private String activityStartDate;
	@Schema(description = "활동 종료 일자", example = "2024년 12월 31일")
	private String activityEndDate;
	@Schema(description = "활동 내용", example = "여러분의 창의력과 디자인 역량을 발휘해볼 특별한 기회를 놓치지 마세요")
	private String content;
	@Schema(description = "공고 url", example = "https://www.google.com/")
	private String detailUrl;
	@Schema(description = "이메일 url", example = "https://www.s3.bucket/")
	private String imageUrl;

	@Getter
	@AllArgsConstructor
	public static class InfoPostInfo {
		private String title;
		private Integer categoryId;
		private String organization;
		private LocalDate recruitmentStartDate;
		private LocalDate recruitmentEndDate;
		private LocalDate activityStartDate;
		private LocalDate activityEndDate;
		private String content;
		private String detailUrl;
		private String imageUrl;
	}

	public static InfoPostUpdateRes of(InfoPostUpdateRes.InfoPostInfo infoPostInfo, List<String> positionInfos) {
		return InfoPostUpdateRes.builder()
			.title(infoPostInfo.getTitle())
			.categoryId(infoPostInfo.getCategoryId())
			.organization(infoPostInfo.getOrganization())
			.recruitmentStartDate(formatDate(infoPostInfo.getRecruitmentStartDate()))
			.recruitmentEndDate(formatDate(infoPostInfo.getRecruitmentEndDate()))
			.positionInfos(positionInfos)
			.activityStartDate(formatDate(infoPostInfo.getActivityStartDate()))
			.activityEndDate(formatDate(infoPostInfo.getActivityEndDate()))
			.content(infoPostInfo.getContent())
			.detailUrl(infoPostInfo.getDetailUrl())
			.imageUrl(infoPostInfo.getImageUrl())
			.build();
	}
}
