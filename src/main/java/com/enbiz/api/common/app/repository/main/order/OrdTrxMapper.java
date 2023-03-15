package com.enbiz.api.common.app.repository.main.order;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.enbiz.api.common.app.entity.Ord;

@Repository
@Lazy
public interface OrdTrxMapper {

	int save(Ord ord);
	
	int update(Ord ord);
}
