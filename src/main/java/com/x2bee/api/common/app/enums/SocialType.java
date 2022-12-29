package com.x2bee.api.common.app.enums;

import org.apache.commons.lang3.StringUtils;

public enum SocialType {
	KAKAO, NAVER;

	public static SocialType ofType(String type) {
		return SocialType.valueOf(StringUtils.upperCase(type));
	}
}