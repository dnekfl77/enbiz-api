package com.enbiz.api.common.app.dto.response.order;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.enbiz.api.common.app.enums.PayState;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayResponse implements Serializable {
	private static final long serialVersionUID = 3307069346867688103L;

	private Long id;
	private PayState payState;
	private String orderNo;
	private String resMsg;
	private String resCd;
	private String pgTxid;
	private Long amount;
	private String traceNo;
	private String tno;
	private String payMethod;
	private LocalDateTime createDt;
}
