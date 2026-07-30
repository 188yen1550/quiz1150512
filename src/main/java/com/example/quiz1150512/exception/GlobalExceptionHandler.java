package com.example.quiz1150512.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.quiz1150512.Response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	// 1. 攔截業務邏輯例外 (例如：結束日期早於開始日期、選擇題無選項)
	@ExceptionHandler({ IllegalArgumentException.class, RuntimeException.class })
	public ResponseEntity<ApiResponse> handleRuntimeException(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(e.getMessage()));
	}
	// 2. 攔截 @Valid 驗證失敗的例外 (如：標題為空)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException e) {
		String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(defaultMessage));
	}
	// 3. 攔截其他未知的系統例外
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleGeneralException(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ApiResponse.failure("Internal Server Error: " + e.getMessage()));
	}
}

