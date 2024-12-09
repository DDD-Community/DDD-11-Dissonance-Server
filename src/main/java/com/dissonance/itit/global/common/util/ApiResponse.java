package com.dissonance.itit.global.common.util;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
	private final int code;
	private final String message;
	private final T data;

	private ApiResponse(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(HttpStatus.OK.value(), null, data);
	}

	public static ApiResponse<?> error(int code, String message) {
		return new ApiResponse<>(code, message, null);
	}
}