package com.dissonance.itit.bookmark.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkRes {
	private final Long postId;
	private final String title;
	private final String remainingDays;
	private final String deadLine;

	public static List<BookmarkRes> of(List<BookmarkRes.BookmarkedPost> bookmarkedPosts) {
		return bookmarkedPosts.stream()
			.map(postInfo -> BookmarkRes.builder()
				.postId(postInfo.postId())
				.title(postInfo.title())
				.remainingDays(calculateRemainingDays(postInfo.deadline()))
				.deadLine(parseDeadLine(postInfo.deadline()))
				.build())
			.toList();
	}

	private static String parseDeadLine(LocalDate deadline) {
		return deadline.format(DateTimeFormatter.ofPattern("MM월 dd일 마감"));
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

	public record BookmarkedPost(
		Long postId,
		String title,
		LocalDate deadline) {
	}
}
