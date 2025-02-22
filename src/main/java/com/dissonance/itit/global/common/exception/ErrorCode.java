package com.dissonance.itit.global.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	// 400
	INVALID_PROVIDER(HttpStatus.BAD_REQUEST, "존재하지 않는 Provider입니다."),
	INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "파일 형식은 이미지만 가능합니다."),
	INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "날짜 변환에 실패했습니다."),
	INVALID_APPLE_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않는 Apple Token입니다."),
	INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 JSON 형식입니다."),
	INVALID_SEARCH_KEYWORD_LENGTH(HttpStatus.BAD_REQUEST, "검색어는 100자 이하로 입력해야 합니다."),
	INVALID_SEARCH_KEYWORD_SPECIAL_CHAR(HttpStatus.BAD_REQUEST, "검색어에 허용되지 않는 특수문자가 포함되어 있습니다."),
	INVALID_SEARCH_KEYWORD_SQL_INJECTION(HttpStatus.BAD_REQUEST, "검색어에 사용할 수 없는 예약어가 포함되어 있습니다."),

	// 401
	UNAUTHORIZED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "권한없는 Refresh Token입니다."),

	// 403
	NO_INFO_POST_UPDATE_PERMISSION(HttpStatus.FORBIDDEN, "게시글 수정 권한이 없습니다."),

	// 404
	NON_EXISTENT_USER_ID(HttpStatus.NOT_FOUND, "해당 id의 사용자가 존재하지 않습니다."),
	NON_EXISTENT_EMAIL(HttpStatus.NOT_FOUND, "해당 email의 사용자가 존재하지 않습니다."),
	IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 id의 이미지가 존재하지 않습니다."),
	NON_EXISTENT_CATEGORY_ID(HttpStatus.NOT_FOUND, "해당 id의 카테고리가 존재하지 않습니다."),
	NON_EXISTENT_INFO_POST_ID(HttpStatus.NOT_FOUND, "해당 id의 공고 게시글이 존재하지 않습니다."),
	NON_EXISTENT_FEATURED_INFO_POST_ID(HttpStatus.NOT_FOUND, "해당 id의 추천 공고 게시글이 존재하지 않습니다."),

	// 500
	IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "파일 입출력 에러"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러"),
	POST_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "DB 오류로 게시글 생성에 실패했습니다."),

	// 503
	IMAGE_UPDATE_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "이미지 서버가 일시적으로 요청을 처리할 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}