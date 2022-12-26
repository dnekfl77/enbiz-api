package com.x2bee.api.common.app.repository.displayrwdb.sample;

import java.util.Map;

import com.x2bee.api.common.app.entity.Sample;

public interface SampleTrxMapper {
	void insertSample(Sample sample);
	void updateSample(Sample sample);
	void deleteSample(Long id);
	void insertStCrcCd(Map<String,Object> param);
}
