package com.x2bee.api.common.app.service.payment;

import java.util.Map;

public interface NaverPayService {

	Map<String, Object> payment(String paymentId);

	Map<String, Object> cancel(String paymentId, long cancelAmount, String reason);

	Map<String, Object> purchaseConfirm(String paymentId, String clientId, String clientSecret);

}
