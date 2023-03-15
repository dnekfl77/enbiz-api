package com.enbiz.api.common.app.dto.response.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class OrderPrepareResponse {

	private String orderNo;
}
