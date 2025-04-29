package com.dissonance.itit.post.dto.response;

import static com.dissonance.itit.global.common.util.DateUtil.*;

import java.time.LocalDate;
import java.util.List;

import com.dissonance.itit.post.domain.InfoPost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InfoPostDetailRes {
	private final String title;
	private final String categoryName;
	private final String organization;
	private final String recruitmentPeriod;
	private final List<String> positionInfos;
	private final String activityPeriod;
	private final String content;
	private final String detailUrl;
	private final Integer viewCount;
	private final String imageUrl;
	private final Boolean isBookmarked;

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
		private String imageUrl;
	}

	public static InfoPostDetailRes of(InfoPostInfo infoPostInfo, List<String> positionInfos, boolean isBookmarked) {
		return InfoPostDetailRes.builder()
			.title(infoPostInfo.getTitle())
			.categoryName(infoPostInfo.getCategoryName())
			.organization(infoPostInfo.getOrganization())
			.recruitmentPeriod(
				formatPeriod(infoPostInfo.getRecruitmentStartDate(), infoPostInfo.getRecruitmentEndDate()))
			.positionInfos(positionInfos)
			.activityPeriod(
				formatPeriod(infoPostInfo.getActivityStartDate(), infoPostInfo.getActivityEndDate()))
			.content(infoPostInfo.getContent())
			.detailUrl(infoPostInfo.getDetailUrl())
			.imageUrl(infoPostInfo.getImageUrl())
			.viewCount(infoPostInfo.getViewCount())
			.isBookmarked(isBookmarked)
			.build();
	}

	public static InfoPostDetailRes of(InfoPost infoPost, List<String> positionInfos) {
		return InfoPostDetailRes.builder()
			.title(infoPost.getTitle())
			.categoryName(infoPost.getCategory().getName())
			.organization(infoPost.getOrganization())
			.recruitmentPeriod(
				formatPeriod(infoPost.getRecruitmentStartDate(), infoPost.getRecruitmentEndDate()))
			.positionInfos(positionInfos)
			.activityPeriod(
				formatPeriod(infoPost.getActivityStartDate(), infoPost.getActivityEndDate()))
			.content(infoPost.getContent())
			.detailUrl(infoPost.getDetailUrl())
			.imageUrl(infoPost.getImage().getImageUrl())
			.viewCount(infoPost.getViewCount())
			.isBookmarked(null)
			.build();
	}
}