package com.dissonance.itit.dto.response;

import static com.dissonance.itit.common.util.DateUtil.*;

import java.time.LocalDate;
import java.util.List;

import com.dissonance.itit.domain.entity.InfoPost;

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
	private final List<String> positionInfos;
	private final String activityPeriod;
	private final String content;
	private final String detailUrl;
	private final Integer viewCount;
	private final String imageUrl;

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
		private final String imageUrl;
	}

	public static InfoPostDetailRes of(InfoPostInfo infoPostInfo, List<String> positionInfos) {
		return InfoPostDetailRes.builder()
			.title(infoPostInfo.getTitle())
			.categoryName(infoPostInfo.getCategoryName())
			.organization(infoPostInfo.getOrganization())
			.RecruitmentPeriod(
				formatPeriod(infoPostInfo.getRecruitmentStartDate(), infoPostInfo.getRecruitmentEndDate()))
			.positionInfos(positionInfos)
			.activityPeriod(
				formatPeriod(infoPostInfo.getActivityStartDate(), infoPostInfo.getActivityEndDate()))
			.content(infoPostInfo.getContent())
			.detailUrl(infoPostInfo.getDetailUrl())
			.imageUrl(infoPostInfo.getImageUrl())
			.viewCount(infoPostInfo.getViewCount())
			.build();
	}

	public static InfoPostDetailRes of(InfoPost infoPost, List<String> positionInfos) {
		return InfoPostDetailRes.builder()
			.title(infoPost.getTitle())
			.categoryName(infoPost.getCategory().getName())
			.organization(infoPost.getOrganization())
			.RecruitmentPeriod(
				formatPeriod(infoPost.getRecruitmentStartDate(), infoPost.getRecruitmentEndDate()))
			.positionInfos(positionInfos)
			.activityPeriod(
				formatPeriod(infoPost.getActivityStartDate(), infoPost.getActivityEndDate()))
			.content(infoPost.getContent())
			.detailUrl(infoPost.getDetailUrl())
			.imageUrl(infoPost.getImage().getImageUrl())
			.viewCount(infoPost.getViewCount())
			.build();
	}
}