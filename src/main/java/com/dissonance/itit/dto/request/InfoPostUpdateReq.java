package com.dissonance.itit.dto.request;

import static com.dissonance.itit.common.util.DateUtil.*;

import java.time.LocalDate;

import com.dissonance.itit.domain.entity.Category;

import lombok.Builder;

@Builder
public record InfoPostUpdateReq(
	Category category,
	String title,
	String organization,
	String content,
	LocalDate recruitmentStartDate,
	LocalDate recruitmentEndDate,
	LocalDate activityStartDate,
	LocalDate activityEndDate,
	String detailUrl
) {
	public static InfoPostUpdateReq from(Category category, InfoPostReq req) {
		return InfoPostUpdateReq.builder()
			.category(category)
			.title(req.title())
			.organization(req.organization())
			.content(req.content())
			.recruitmentStartDate(stringToDate(req.recruitmentStartDate()))
			.recruitmentEndDate(stringToDate(req.recruitmentEndDate()))
			.activityStartDate(stringToDate(req.activityStartDate()))
			.activityEndDate(stringToDate(req.activityEndDate()))
			.detailUrl(req.detailUrl())
			.build();
	}
}
