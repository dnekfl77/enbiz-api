package com.x2bee.api.common.app.dto.response.payment;

import lombok.Data;

@Data
public class TossReadyResponse {
	private Integer code;
	private String result;
	private String msg;
	private Integer status;
	private String errorCode;
	private String payToken;
	private String checkoutPage;
}
