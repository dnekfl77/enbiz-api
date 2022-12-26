package com.x2bee.api.common.app.dto.response.delivery;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

/**
 * ==========================
 * 스윗트래커 운송장 추적요청 콜백 Response Dto
 * ==========================
 */

@Alias("SweetTrackerCallBackResponse")
@Setter
@Getter
public class SweetTrackerCallBackResponse  {

    private boolean code			;	//성공여부(true, false)
	private String message			;	//실패사유(success, fail)
}
