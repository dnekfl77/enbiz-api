package com.x2bee.api.common.app.service.interfacecommon;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import com.x2bee.common.base.rest.RestResponse;
import com.x2bee.common.base.rest.infapi.InfRequest;

public interface InfRequestService {

	RestResponse<LinkedHashMap<String,Object>> postOneRequest(InfRequest ifRequest);
	RestResponse<LinkedHashSet<Object>> postRequest(InfRequest ifRequest);
	RestResponse<String> postTextRequest(InfRequest infRequest);
}
