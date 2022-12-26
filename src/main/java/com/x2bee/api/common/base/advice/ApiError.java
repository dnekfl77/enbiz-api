package com.x2bee.api.common.base.advice;

import com.x2bee.common.base.exception.AppError;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiError implements AppError {
	// success
	SUCCESS("0000", "common.message.success"),
	// app error
	EMPTY_PARAMETER("1001", "common.error.emptyParameter"),
	INVALID_PARAMETER("1002", "common.error.invalidParameter"),
	DATA_NOT_FOUND("1003", "common.error.dataNotFound"),
	// 권한 없음.
	NOT_AUTHORIZED("7000", "common.error.notAuthorized"),
	// binding error
	BINDING_ERROR("8000", "common.error.bindingError"),
	BINDING_ERROR_NOT_NULL("8001", "common.error.bindingErrorNotNull"),
	// unknow error
	UNKNOWN("9000", "common.error.unknown");

	private final String code;
	private final String messageKey;

}
