package com.x2bee.api.common.app.service.order.impl;

import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.github.underscore.U;
import com.x2bee.api.common.app.service.order.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Lazy
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	@Value("${payment.kcp.siteCd}")
	private String kcpSiteCd;
	@Value("${payment.kcp.siteName}")
	private String kcpSiteName;
	@Value("${payment.kcp.jsUrl}")
	private String kcpJsUrl;
	
	@Override
	public Map<String, Object> fetchPayInfo() {
		log.debug("fetchPayInfo");

		return U.objectBuilder()
				.add("kcpSiteCd", kcpSiteCd)
				.add("kcpSiteName", kcpSiteName)
				.add("kcpJsUrl", kcpJsUrl)
				.add("orderXxx", RandomStringUtils.randomAlphanumeric(10))
				.add("goodName", "test_good_name")
				.add("goodMny", 1004)
				.build();
	}
}
