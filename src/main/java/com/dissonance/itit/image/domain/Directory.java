package com.dissonance.itit.image.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Directory {
	INFORMATION("info-posts"),
	RECRUITMENT("recruit-posts"),
	FEATURE("featured-posts");

	private final String name;
}
