package com.x2bee.api.common.app.repository.displayrodb.sample;

import java.util.List;
import java.util.Optional;

import com.x2bee.api.common.app.dto.request.sample.SampleRequest;
import com.x2bee.api.common.app.dto.response.sample.SampleResponse;

public interface SampleMapper {
	public List<SampleResponse> selectAllSamples();
	public Optional<SampleResponse> selectSampleById(Long id);
	public List<SampleResponse> selectSamples(SampleRequest request);
}
