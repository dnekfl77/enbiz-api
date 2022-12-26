package com.x2bee.api.common.app.service.interfacecommon;

import com.x2bee.common.base.rest.RestResponse;
import com.x2bee.common.base.rest.erpapi.ErpRequest;

public interface ErpRequestService {

	RestResponse<String> postRequest(ErpRequest erpRequest);

}
