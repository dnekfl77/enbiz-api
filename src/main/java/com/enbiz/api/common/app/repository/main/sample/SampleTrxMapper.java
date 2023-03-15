package com.enbiz.api.common.app.repository.main.sample;

import java.util.Map;

import com.enbiz.api.common.app.entity.Sample;

public interface SampleTrxMapper {
	void insertSample(Sample sample);
	void updateSample(Sample sample);
	void deleteSample(Long id);
	void insertStCrcCd(Map<String,Object> param);
}
