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
	
	//nice code (61XX)
	FAIL_NICE_ENCRYPTION_SYSTEM_ERROR("6101", "nice.encryption.system.error"),
	FAIL_NICE_ENCRYPTION_PROCESS_ERROR("6102", "nice.encryption.process.error"),
	FAIL_NICE_ENCRYPTION_DATA_ERROR("6103", "nice.encryption.data.error"),
	FAIL_NICE_DECRYPTION_SYSTEM_ERROR("6104", "nice.decryption.system.error"),
	FAIL_NICE_DECRYPTION_PROCESS_ERROR("6105", "nice.decryption.process.error"),
	FAIL_NICE_DECRYPTION_HASH_ERROR("6106", "nice.decryption.hash.error"),
	FAIL_NICE_DECRYPTION_DATA_ERROR("6107", "nice.decryption.data.error"),
	FAIL_NICE_SITE_PASSWORD_ERROR("6108", "nice.site.password.error"),
	FAIL_NICE_INPUT_DATA_ERROR("6110", "nice.input.data.error"),
	FAIL_NICE_UNKNOWN_ERROR("6199", "nice.unknown.error"),
	
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
