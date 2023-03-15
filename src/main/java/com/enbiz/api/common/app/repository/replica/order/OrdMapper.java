package com.enbiz.api.common.app.repository.replica.order;

import java.util.List;

import com.enbiz.api.common.app.dto.request.order.OrdRequest;
import com.enbiz.api.common.app.dto.response.order.OrdResponse;

public interface OrdMapper {

	List<OrdResponse> listByRequest(OrdRequest request);
}
