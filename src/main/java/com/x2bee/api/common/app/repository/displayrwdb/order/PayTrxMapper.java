package com.x2bee.api.common.app.repository.displayrwdb.order;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.x2bee.api.common.app.entity.Pay;

@Lazy
@Repository
public interface PayTrxMapper {

	int save(Pay pay);
	
	int cancel(Pay pay);
}
