package com.dissonance.itit.bookmark.dto;

import java.util.List;

import org.springframework.data.domain.Slice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomSliceRes<T> {
	private final boolean hasNest;
	private final List<T> content;

	public static <T> CustomSliceRes<T> from(Slice<T> slice) {
		return new CustomSliceRes<>(slice.hasNext(), slice.getContent());
	}
}
