package com.x2bee.api.common.app.service.delivery;

import com.x2bee.api.common.app.dto.request.delivery.SweetTrackerCallBackRequest;
import com.x2bee.api.common.app.dto.response.delivery.SweetTrackerCallBackResponse;
import com.x2bee.common.base.rest.Response;

public interface DeliveryApiService {
	Response<SweetTrackerCallBackResponse> sweetTrackerCallBack(SweetTrackerCallBackRequest sweetTrackerCallBackRequest)throws Exception;

}
