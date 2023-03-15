package com.enbiz.api.common.app.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.enbiz.api.common.app.enums.PayState;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class Pay implements Serializable {
	private static final long serialVersionUID = -5696698117239960417L;

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
