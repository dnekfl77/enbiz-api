package com.x2bee.api.common.app.dto.request.order;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class PayRequest implements Serializable {
	private static final long serialVersionUID = 5022681037646774096L;

	private Long id;
	private Long amount;
	private String orderNo;
	private String payMethod;
	private String pgTxid;
	private String tno;
	private String traceNo;
}
