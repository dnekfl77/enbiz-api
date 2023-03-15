package com.enbiz.api.common.app.service.payment;

import java.util.Map;

public interface KakaoPayService {

	Map<String, Object> ready(String partnerOrderId, String partnerUserId, String itemName, Long quantity, Long totalAmount, String approvalUrl, String cancelUrl, String failUrl);

	Map<String, Object> approve(Map<String, Object> request);

	Map<String, Object> cancel(Map<String, Object> request);

}