package com.x2bee.api.common.app.repository.displayrodb.order;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.x2bee.api.common.app.dto.request.order.PayRequest;
import com.x2bee.api.common.app.dto.response.order.PayResponse;

@Lazy
@Repository
public interface PayMapper {

	List<PayResponse> listByRequest(PayRequest request);
}
