package com.enbiz.api.common.app.dto.response.order;

import java.time.LocalDateTime;

import com.enbiz.api.common.app.enums.TargetPg;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class OrdResponse {

	private Long id;
	private String orderNo;
	private String goodsName;
	private Long goodsPrice;
	private String buyerName;
	private String buyerPhone;
	private String buyerEmail;
	private TargetPg targetPg;
	private LocalDateTime createDt;
	private LocalDateTime updateDt;

	private PayResponse pay;
}
