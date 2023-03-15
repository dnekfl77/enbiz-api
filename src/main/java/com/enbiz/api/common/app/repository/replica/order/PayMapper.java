package com.enbiz.api.common.app.repository.replica.order;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.enbiz.api.common.app.dto.request.order.PayRequest;
import com.enbiz.api.common.app.dto.response.order.PayResponse;

@Lazy
@Repository
public interface PayMapper {

	List<PayResponse> listByRequest(PayRequest request);
}
