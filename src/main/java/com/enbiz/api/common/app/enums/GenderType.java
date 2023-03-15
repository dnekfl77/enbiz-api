package com.enbiz.api.common.app.enums;

import org.apache.commons.lang3.StringUtils;

public enum GenderType {
	MALE, FEMALE;

	public static GenderType of(String gender) {
		return StringUtils.equalsAnyIgnoreCase(gender, "1", "male") ? MALE : StringUtils.equalsAnyIgnoreCase(gender, "2", "female") ? FEMALE : null;
	}
}
