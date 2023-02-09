package com.x2bee.api.common.app.service.payment;

import com.x2bee.api.common.app.dto.request.payment.TossReadyRequest;
import com.x2bee.api.common.app.dto.response.payment.TossReadyResponse;

public interface TossPayService {

	TossReadyResponse ready(TossReadyRequest request);

}
