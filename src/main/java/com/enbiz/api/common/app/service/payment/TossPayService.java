package com.enbiz.api.common.app.service.payment;

import com.enbiz.api.common.app.dto.request.payment.TossApproveRequest;
import com.enbiz.api.common.app.dto.request.payment.TossReadyRequest;
import com.enbiz.api.common.app.dto.response.payment.TossApproveResponse;
import com.enbiz.api.common.app.dto.response.payment.TossReadyResponse;

public interface TossPayService {

	TossReadyResponse ready(TossReadyRequest request);
	
	TossApproveResponse approve(TossApproveRequest request);

}
