package com.dissonance.itit.global.common.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.dissonance.itit.global.common.exception.CustomException;
import com.dissonance.itit.global.common.exception.ErrorCode;

public class SearchValidator {
	// 허용할 특수문자 패턴 (공백, 하이픈, 언더스코어만 허용)
	private static final Pattern ALLOWED_CHARS =
		Pattern.compile("^[가-힣a-zA-Z0-9\\s\\-_]+$");

	// SQL Injection 위험이 있는 키워드들
	private static final List<String> SQL_INJECTION_PATTERNS = Arrays.asList(
		"SELECT", "INSERT", "UPDATE", "DELETE", "DROP",
		"UNION", "INTO", "FROM", "WHERE", "--", "/*",
		"*", "*/", ";", "OR", "AND"
	);

	public static void validateSearchKeyword(String keyword) {
		// 1. 기본 길이 검증
		if (keyword.length() > 100) {
			throw new CustomException(ErrorCode.INVALID_SEARCH_KEYWORD_LENGTH);
		}

		// 2. 특수문자 검증
		if (!ALLOWED_CHARS.matcher(keyword).matches()) {
			throw new CustomException(ErrorCode.INVALID_SEARCH_KEYWORD_SPECIAL_CHAR);
		}

		// 3. SQL Injection 패턴 검증
		String upperKeyword = keyword.toUpperCase();
		for (String pattern : SQL_INJECTION_PATTERNS) {
			if (upperKeyword.contains(pattern)) {
				throw new CustomException(ErrorCode.INVALID_SEARCH_KEYWORD_SQL_INJECTION);
			}
		}
	}
}