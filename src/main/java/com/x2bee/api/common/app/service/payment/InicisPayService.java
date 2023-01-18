package com.x2bee.api.common.app.service.payment;

import java.util.Map;

public interface InicisPayService {

	Map<String, Object> payment(String authUrl, String netCancelUrl, String mid, String authToken, String timestamp);

}
