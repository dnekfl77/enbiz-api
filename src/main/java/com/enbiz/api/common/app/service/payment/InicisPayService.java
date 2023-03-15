package com.enbiz.api.common.app.service.payment;

import java.util.Map;

public interface InicisPayService {

	Map<String, Object> payment(String authUrl, String netCancelUrl, String mid, String authToken, String timestamp);

	Map<String, Object> cancel(String payMethod, String type, String timestamp, String clientIp, String mid, String tid, String msg);

	Map<String, Object> netCancel(String netCancelUrl, String mid, String authToken, String timestamp);
}
