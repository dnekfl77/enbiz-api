package com.enbiz.api.common.app.service.certification.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.enbiz.api.common.app.service.certification.CertificationService;
import com.enbiz.api.common.base.advice.ApiError;
import com.enbiz.common.base.exception.AppException;
import com.enbiz.common.base.exception.MessageResolver;
import com.github.underscore.U;

import NiceID.Check.CPClient;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Lazy
public class CertificationServiceImpl implements CertificationService {
	
	@Value("${nice.cert.siteCode:}")
	private String siteCode;
	@Value("${nice.cert.sitePassword:}")
	private String sitePassword;
	
	@Override
	public Map<String, Object> encodeNiceRequestData(Map<String, Object> params) {
		
		var deviceType = MapUtils.getString(params, "deviceType", "PC");
		var customize = !deviceType.equalsIgnoreCase("PC") ? "Mobile" : ""; // "Mobile"; // 없으면 기본 웹페이지 / Mobile : 모바일페이지
		var authType = MapUtils.getString(params, "authType", ""); // 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서
		var gender = MapUtils.getString(params, "gender", ""); //없으면 기본 선택 값, 0 : 여자, 1 : 남자 
		var popgubun = MapUtils.getString(params, "popgubun", "N"); // Y : 취소버튼 있음 / N : 취소버튼 없음
		var returnUrl = MapUtils.getString(params, "returnUrl", "");
		var errorUrl = MapUtils.getString(params, "errorUrl", "");
		var requestNumber = MapUtils.getString(params, "requestNumber", "");
		
		var plainData = "7:REQ_SEQ" + requestNumber.getBytes().length + ":" + requestNumber +
				"8:SITECODE" + siteCode.getBytes().length + ":" + siteCode +
				"9:AUTH_TYPE" + authType.getBytes().length + ":" + authType +
				"7:RTN_URL" + returnUrl.getBytes().length + ":" + returnUrl +
				"7:ERR_URL" + errorUrl.getBytes().length + ":" + errorUrl +
				"11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
				"9:CUSTOMIZE" + customize.getBytes().length + ":" + customize + 
				"6:GENDER" + gender.getBytes().length + ":" + gender;
		
		var cpClient = new CPClient();
		var result = cpClient.fnEncode(siteCode, sitePassword, plainData);
		if (result != 0) {
			throw handleNiceEncodeException(result);
		}
		return U.objectBuilder()
				.add("cipherData", cpClient.getCipherData())
				.add("returnUrl", returnUrl)
				.add("errorUrl", errorUrl)
				.build();
		}

	//{GENDER=1, RES_SEQ=MG7375202206210676615241, BIRTHDATE=19770901, NATIONALINFO=0, AUTH_TYPE=M, NAME=장진희}
		@Override
		public Map<String, Object>  decodeNiceResponseData(Map<String, Object> params) {
			var cpClient = new CPClient();
			var result = cpClient.fnDecode(siteCode, sitePassword, MapUtils.getString(params, "encodeData"));
			if (result != 0) {
				throw handleNiceDecodeException(result);
			}
			
			log.debug("result: {}", result);
			log.debug("cipherData: {}", cpClient.getCipherData());
			log.debug("plainData: {}", cpClient.getPlainData());
			log.debug("parseData: {}", cpClient.fnParse(cpClient.getPlainData()));
			
			return Optional.of(cpClient.fnParse(cpClient.getPlainData())).map(o -> {
				return U.objectBuilder()
						.add("reqSeq", MapUtils.getString(o, "REQ_SEQ"))
						.add("mobileNo",MapUtils.getString(o, "MOBILE_NO"))
						.add("di",MapUtils.getString(o, "DI"))
						.add("ci",MapUtils.getString(o, "CI"))
						.add("gender",MapUtils.getString(o, "GENDER"))
						.add("resSeq",MapUtils.getString(o, "RES_SEQ"))
						.add("authType",MapUtils.getString(o, "AUTH_TYPE"))
						.add("name",MapUtils.getString(o, "NAME"))
						.add("birthdate",LocalDate.parse(MapUtils.getString(o, "BIRTHDATE"), DateTimeFormatter.ofPattern("yyyyMMdd")))
						.build();
			}).get();
		}
	
	private AppException handleNiceEncodeException(int code) {
		switch (code) {
		case -1:
			return AppException.exception(ApiError.FAIL_NICE_ENCRYPTION_SYSTEM_ERROR);
		case -2:
			return AppException.exception(ApiError.FAIL_NICE_ENCRYPTION_PROCESS_ERROR);
		case -3:
			return AppException.exception(ApiError.FAIL_NICE_ENCRYPTION_DATA_ERROR);
		case -9:
			return AppException.exception(ApiError.FAIL_NICE_INPUT_DATA_ERROR);
		default:
			return new AppException(ApiError.FAIL_NICE_UNKNOWN_ERROR.getCode(),
					MessageResolver.getMessage(ApiError.FAIL_NICE_UNKNOWN_ERROR, new Object[] { code }));
		}
	}
	
	private AppException handleNiceDecodeException(int code) {
		switch (code) {
		case -1:
			return AppException.exception(ApiError.FAIL_NICE_DECRYPTION_SYSTEM_ERROR);
		case -4:
			return AppException.exception(ApiError.FAIL_NICE_DECRYPTION_PROCESS_ERROR);
		case -5:
			return AppException.exception(ApiError.FAIL_NICE_DECRYPTION_HASH_ERROR);
		case -6:
			return AppException.exception(ApiError.FAIL_NICE_DECRYPTION_DATA_ERROR);
		case -9:
			return AppException.exception(ApiError.FAIL_NICE_INPUT_DATA_ERROR);
		case -12:
			return AppException.exception(ApiError.FAIL_NICE_SITE_PASSWORD_ERROR);
		default:
			return new AppException(ApiError.FAIL_NICE_UNKNOWN_ERROR.getCode(),
					MessageResolver.getMessage(ApiError.FAIL_NICE_UNKNOWN_ERROR, new Object[] { code }));
		}
	}
}
