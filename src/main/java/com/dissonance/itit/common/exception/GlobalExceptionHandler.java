package com.dissonance.itit.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.dissonance.itit.common.util.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
		int statusCode = e.getErrorCode().getHttpStatus().value();
		log.error("CustomException : {}", e.getMessage());
		return new ResponseEntity<>(ApiResponse.error(statusCode, e.getMessage()),
			e.getErrorCode().getHttpStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<?>> handleAllException(final Exception e) {
		int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
		log.error("handleAllException {}", e.getMessage());
		return new ResponseEntity<>(ApiResponse.error(statusCode, e.getMessage()),
			HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ApiResponse<?>> handleMaxSizeException(MaxUploadSizeExceededException e) {
		int statusCode = e.getStatusCode().value();
		log.error("MaxUploadSizeExceededException {}", e.getMessage());
		return new ResponseEntity<>(ApiResponse.error(statusCode, e.getMessage()),
			HttpStatus.PAYLOAD_TOO_LARGE);
	}
}
