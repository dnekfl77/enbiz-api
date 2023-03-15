package com.enbiz.api.common.app.repository.main.order;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.enbiz.api.common.app.entity.Pay;

@Lazy
@Repository
public interface PayTrxMapper {

	int save(Pay pay);
	
	int cancel(Pay pay);
}
