package com.x2bee.api.common.app.enums;

public enum TargetPg {
	KCP, INISIS, KAKAO;

	TargetPg toTargetPg(String value) {
		try {
			return TargetPg.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}
}
