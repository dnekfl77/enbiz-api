package com.x2bee.api.common.app.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@Data
public class TossReadyRequest {

	private String orderNo;
	private Long amount;
	private Long amountTaxFree;
	private String productDesc;
	private String apiKey;
	private Boolean autoExecute;
	private String resultCallback;
	private String retUrl;
	private String retCancelUrl;
	
	public static TossReadyRequest from(PayReadyRequest request) {
		return TossReadyRequest.builder()
				.orderNo(request.getOrderNo())
				.amount(request.getGoodsPrice())
				.amountTaxFree(0L)
				.productDesc(request.getGoodsName())
				.autoExecute(true)
				.resultCallback(request.getCallbackUrl())
				.retUrl(request.getReturnUrl())
				.retCancelUrl(request.getCancelUrl())
				.build();
	}
}
