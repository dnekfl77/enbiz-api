package com.x2bee.api.common.base.utils;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.base.Joiner;

public class ShaCryptUtils {

	public static String encryptSha512(String ... values) {
		return DigestUtils.sha512Hex(Joiner.on("").join(values));
	}
}
