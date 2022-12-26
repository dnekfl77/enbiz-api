package com.x2bee.api.common.app.repository.displayrwdb.inf;

import org.apache.ibatis.annotations.Mapper;

import com.x2bee.api.common.app.entity.StInfLog;

@Mapper
public interface StInfLogTrxMapper {
	public void insertStInfLog(StInfLog stInfLog);
}
