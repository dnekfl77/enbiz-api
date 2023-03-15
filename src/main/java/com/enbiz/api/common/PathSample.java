package com.enbiz.api.common;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

public class PathSample {

	public static void main(String[] args) throws Exception {
		;
		System.out.println(IOUtils.toString(new ClassPathResource("common/mapper/mybatis-config.xml").getInputStream(), "UTF-8"));
	}
}
