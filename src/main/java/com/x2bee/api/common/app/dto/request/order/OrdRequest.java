package com.x2bee.api.common.app.dto.request.order;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.x2bee.api.common.app.enums.TargetPg;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class OrdRequest implements Serializable {
	private static final long serialVersionUID = -1070156856684582402L;

	private Long id;
	private String orderNo;
	private String goodsName;
	private Long goodsPrice;
	private String buyerName;
	private String buyerPhone;
	private String buyerEmail;
	private TargetPg targetPg;

	@JsonProperty("pay")
	private PayRequest payRequest;

}
