package com.enbiz.api.common.app.repository.replica.sample;

import java.util.List;
import java.util.Optional;

import com.enbiz.api.common.app.dto.request.sample.SampleRequest;
import com.enbiz.api.common.app.dto.response.sample.SampleResponse;

public interface SampleMapper {
	public List<SampleResponse> selectAllSamples();
	public Optional<SampleResponse> selectSampleById(Long id);
	public List<SampleResponse> selectSamples(SampleRequest request);
}
