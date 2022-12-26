package com.x2bee.api.common.app.service.interfaces;

import java.util.List;

import com.x2bee.api.common.app.dto.request.sample.SampleRequest;
import com.x2bee.api.common.app.dto.response.sample.SampleResponse;
import com.x2bee.common.base.rest.Response;

public interface SampleInfService {

	Response<List<SampleResponse>> searchSamples(SampleRequest sampleRequest) throws Exception;

}
