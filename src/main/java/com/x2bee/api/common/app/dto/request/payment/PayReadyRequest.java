package com.x2bee.api.common.app.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@Data
public class PayReadyRequest {
	private String orderNo;
	private Long goodsPrice;
	private String goodsName;
	private String buyerName;
	private String buyerPhone;
	private String buyerEmail;
	private String callbackUrl;
	private String returnUrl;
	private String cancelUrl;
}
