package com.x2bee.api.common.app.repository.displayrodb.order;

import java.util.List;

import com.x2bee.api.common.app.dto.request.order.OrdRequest;
import com.x2bee.api.common.app.dto.response.order.OrdResponse;

public interface OrdMapper {

	List<OrdResponse> listByRequest(OrdRequest request);
}
