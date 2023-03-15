package com.enbiz.api.common.app.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@Data
public class TossApproveRequest {

	private String orderId;
	private String paymentKey;
	private Long amount;
}
