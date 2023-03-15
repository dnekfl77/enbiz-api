package com.enbiz.api.common.app.service.order;

import java.util.List;
import java.util.Map;

import com.enbiz.api.common.app.dto.request.order.OrdRequest;
import com.enbiz.api.common.app.dto.response.order.OrdResponse;

public interface OrderService {

	Map<String, Object> payInfo();

	Map<String, Object> kcpPayInfo();

	Map<String, Object> inicisPayInfo();

	Map<String, Object> kakaoPayReady(Map<String, Object> params);

	Map<String, Object> naverPayInfo();

	Map<String, Object> tossPayInfo();

	Map<String, Object> saveOrder(Map<String, Object> request);

	List<OrdResponse> listOrder(OrdRequest request);

	void cancelOrder(Map<String, Object> params);

	void cancelOrder(OrdRequest request);

}
