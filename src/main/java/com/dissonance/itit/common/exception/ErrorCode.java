package com.dissonance.itit.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	// 400
	INVALID_PROVIDER(HttpStatus.BAD_REQUEST, "존재하지 않는 Provider입니다."),
	INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "파일 형식은 이미지만 가능합니다."),
	INVALID_FILE_SIZE(HttpStatus.BAD_REQUEST, "파일 용량은 10MB를 넘을 수 없습니다."),
	INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "날짜 변환에 실패했습니다."),

	// 404
	NON_EXISTENT_USER_ID(HttpStatus.NOT_FOUND, "해당 id의 사용자가 존재하지 않습니다."),
	NON_EXISTENT_EMAIL(HttpStatus.NOT_FOUND, "해당 email의 사용자가 존재하지 않습니다."),
	IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 id의 이미지가 존재하지 않습니다."),
	NON_EXISTENT_CATEGORY_ID(HttpStatus.NOT_FOUND, "해당 id의 카테고리가 존재하지 않습니다."),
	NON_EXISTENT_INFO_POST_ID(HttpStatus.NOT_FOUND, "해당 id의 공고 게시글이 존재하지 않습니다."),
	REPORTED_INFO_POST_ID(HttpStatus.NOT_FOUND, "해당 id의 게시글은 신고 처리되었습니다."),

	// 500
	IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "파일 입출력 에러");

	private final HttpStatus httpStatus;
	private final String message;
}