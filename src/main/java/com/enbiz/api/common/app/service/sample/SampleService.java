package com.enbiz.api.common.app.service.sample;

import java.util.List;

import com.enbiz.api.common.app.dto.request.sample.SampleRequest;
import com.enbiz.api.common.app.dto.response.sample.SampleResponse;

public interface SampleService {
	public List<SampleResponse> getAllSamples();
	public SampleResponse getSample(Long id);
	public List<SampleResponse> searchSamples(SampleRequest sampleRequest);
	void registerCrcCd() throws Exception;
}
