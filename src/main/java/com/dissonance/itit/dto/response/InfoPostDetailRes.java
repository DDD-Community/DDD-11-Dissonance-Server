package com.dissonance.itit.dto.response;

import static com.dissonance.itit.common.util.DateUtil.*;

import java.time.LocalDate;
import java.util.List;

import com.dissonance.itit.dto.common.PositionInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InfoPostDetailRes {
	private final String title;
	private final String categoryName;
	private final String organization;
	private final String RecruitmentPeriod;
	private final List<PositionInfo> positionInfos;
	private final String activityPeriod;
	private final String content;
	private final String detailUrl;
	private final Integer viewCount;

	@Getter
	@AllArgsConstructor
	public static class InfoPostInfo {
		private String title;
		private String categoryName;
		private String organization;
		private LocalDate recruitmentStartDate;
		private LocalDate recruitmentEndDate;
		private LocalDate activityStartDate;
		private LocalDate activityEndDate;
		private String content;
		private String detailUrl;
		private Integer viewCount;
		private Boolean reported;
	}

	public static InfoPostDetailRes of(InfoPostInfo infoPostInfo, List<PositionInfo> positionInfos) {
		return InfoPostDetailRes.builder()
			.title(infoPostInfo.getTitle())
			.categoryName(infoPostInfo.getCategoryName())
			.organization(infoPostInfo.getOrganization() == null ? "" : infoPostInfo.getOrganization())
			.RecruitmentPeriod(
				formatPeriod(infoPostInfo.getRecruitmentStartDate(), infoPostInfo.getRecruitmentEndDate()))
			.positionInfos(positionInfos)
			.activityPeriod(
				formatPeriod(infoPostInfo.getActivityStartDate(), infoPostInfo.getActivityEndDate()))
			.content(infoPostInfo.getContent() == null ? "" : infoPostInfo.getContent())
			.detailUrl(infoPostInfo.getDetailUrl())
			.viewCount(infoPostInfo.getViewCount())
			.build();
	}
}