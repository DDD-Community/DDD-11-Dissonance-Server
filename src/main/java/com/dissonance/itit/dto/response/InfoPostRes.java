package com.dissonance.itit.dto.response;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InfoPostRes {
	private final Long id;
	private final String imgUrl;
	private final String title;
	private final String remainingDays;

	public static List<InfoPostRes> of(List<InfoPostInfo> postInfos) {
		return postInfos.stream()
			.map(postInfo -> InfoPostRes.builder()
				.id(postInfo.id())
				.imgUrl(postInfo.imgUrl())
				.title(postInfo.title())
				.remainingDays(calculateRemainingDays(postInfo.deadline()))
				.build())
			.toList();
	}

	private static String calculateRemainingDays(LocalDate deadline) {
		LocalDate today = LocalDate.now();
		long daysLeft = ChronoUnit.DAYS.between(today, deadline);

		if (daysLeft > 0) {
			return "D-" + daysLeft;
		} else if (daysLeft == 0) {
			return "D-Day";
		} else {
			return "마감";
		}
	}

	public record InfoPostInfo(
		Long id,
		String imgUrl,
		String title,
		LocalDate deadline) {
	}
}
