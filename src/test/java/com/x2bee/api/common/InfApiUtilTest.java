package com.x2bee.api.common;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.LinkedMultiValueMap;

import com.x2bee.common.base.rest.Response;
import com.x2bee.common.base.rest.infapi.InfApiUtil;
import com.x2bee.common.base.rest.infapi.InfRequest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class InfApiUtilTest {
	@Autowired
	private InfApiUtil infApiUtil;
	
	private String testServerDomain = "http://localhost:8081";

	@Test
	void getSampleById() throws Exception {
		LinkedMultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
		headers.add("test-header-name1", "test header value 1 입니다.");
		headers.add("test-header-name2", "test header value 2 입니다.");

		LinkedMultiValueMap<String,String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("test-queryParam-name1", "test queryParam value 1 입니다.");
		queryParams.add("test-queryParam-name2", "test queryParam value 2 입니다.");

		InfSampleRequest sampleRequest = new InfSampleRequest();
		sampleRequest.setId(2L);
		sampleRequest.setName("sampleTestName");

		InfRequest request = new InfRequest()
				.setIfType("IFTYP_001") 
				.setIfId("IFID_002")
				.setUrl(testServerDomain+ "/api/samples/2")
				.setHeaders(headers)
				.setQueryParams(queryParams)
				.setRequestObject(sampleRequest);
		
		Response<InfSampleResponse> response = infApiUtil.getOne(request, new ParameterizedTypeReference<Response<InfSampleResponse>>() {});
		
		log.debug("result: {}", response);
	}

	@Test
	void searchSamples() throws Exception {
		LinkedMultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
		headers.add("test-header-name1", "test header value 1 입니다.");
		headers.add("test-header-name2", "test header value 2 입니다.");

		LinkedMultiValueMap<String,String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("test-queryParam-name1", "test queryParam value 1 입니다.");
		queryParams.add("test-queryParam-name2", "test queryParam value 2 입니다.");

		InfSampleRequest sampleRequest = new InfSampleRequest();
		sampleRequest.setId(2L);
		sampleRequest.setName("sampleTestName");

		InfRequest request = new InfRequest()
				.setIfType("IFTYP_001") 
				.setIfId("IF_ID_002")
				.setUrl(testServerDomain+ "/api/samples/search")
				.setHeaders(headers)
				.setQueryParams(queryParams)
				.setRequestObject(sampleRequest);
		
		Response<List<InfSampleResponse>> response = infApiUtil.get(request, new ParameterizedTypeReference<Response<List<InfSampleResponse>>>() {});
		
		log.debug("result: {}", response);
	}

	@Test
	void registerSamples() throws Exception {
		LinkedMultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
		headers.add("test-header-name1", "test header value 1 입니다.");
		headers.add("test-header-name2", "test header value 2 입니다.");

		LinkedMultiValueMap<String,String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("test-queryParam-name1", "test queryParam value 1 입니다.");
		queryParams.add("test-queryParam-name2", "test queryParam value 2 입니다.");

		InfSampleRequest sampleRequest = new InfSampleRequest();
		sampleRequest.setId(2L);
		sampleRequest.setName("sampleTestName");

		InfRequest request = new InfRequest()
				.setIfType("IFTYP_001") 
				.setIfId("IF_ID_003")
				.setUrl(testServerDomain+ "/api/samples")
				.setHeaders(headers)
				.setQueryParams(queryParams)
				.setRequestObject(sampleRequest);
		
		Response<Map<String,Object>> response = infApiUtil.post(request, new ParameterizedTypeReference<Response<Map<String,Object>>>() {});
		
		log.debug("result: {}", response);
	}

	@Test
	void modifySamples() throws Exception {
		LinkedMultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
		headers.add("test-header-name1", "test header value 1 입니다.");
		headers.add("test-header-name2", "test header value 2 입니다.");

		LinkedMultiValueMap<String,String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("test-queryParam-name1", "test queryParam value 1 입니다.");
		queryParams.add("test-queryParam-name2", "test queryParam value 2 입니다.");

		InfSampleRequest sampleRequest = new InfSampleRequest();
		sampleRequest.setId(2L);
		sampleRequest.setName("sampleTestName");

		InfRequest request = new InfRequest()
				.setIfType("IFTYP_001") 
				.setIfId("IF_ID_003")
				.setUrl(testServerDomain+ "/api/samples/2")
				.setHeaders(headers)
				.setQueryParams(queryParams)
				.setRequestObject(sampleRequest);
		
		Response<Map<String,Object>> response = infApiUtil.put(request, new ParameterizedTypeReference<Response<Map<String,Object>>>() {});
		
		log.debug("result: {}", response);
	}

	@Test
	void patchSamples() throws Exception {
		LinkedMultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
		headers.add("test-header-name1", "test header value 1 입니다.");
		headers.add("test-header-name2", "test header value 2 입니다.");

		LinkedMultiValueMap<String,String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("test-queryParam-name1", "test queryParam value 1 입니다.");
		queryParams.add("test-queryParam-name2", "test queryParam value 2 입니다.");

		InfSampleRequest sampleRequest = new InfSampleRequest();
		sampleRequest.setId(2L);
		sampleRequest.setName("sampleTestName");

		InfRequest request = new InfRequest()
				.setIfType("IFTYP_001") 
				.setIfId("IF_ID_003")
				.setUrl(testServerDomain+ "/api/samples/2")
				.setHeaders(headers)
				.setQueryParams(queryParams)
				.setRequestObject(sampleRequest);
		
		Response<Map<String,Object>> response = infApiUtil.patch(request, new ParameterizedTypeReference<Response<Map<String,Object>>>() {});
		
		log.debug("result: {}", response);
	}

	@Test
	void deleteSamples() throws Exception {
		LinkedMultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
		headers.add("test-header-name1", "test header value 1 입니다.");
		headers.add("test-header-name2", "test header value 2 입니다.");

		LinkedMultiValueMap<String,String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("test-queryParam-name1", "test queryParam value 1 입니다.");
		queryParams.add("test-queryParam-name2", "test queryParam value 2 입니다.");

		InfSampleRequest sampleRequest = new InfSampleRequest();
		sampleRequest.setId(2L);
		sampleRequest.setName("sampleTestName");

		InfRequest request = new InfRequest()
				.setIfType("IFTYP_001") 
				.setIfId("IF_ID_003")
				.setUrl(testServerDomain+ "/api/samples/2")
				.setHeaders(headers)
				.setQueryParams(queryParams)
				.setRequestObject(sampleRequest);
		
		Response<Map<String,Object>> response = infApiUtil.delete(request, new ParameterizedTypeReference<Response<Map<String,Object>>>() {});
		
		log.debug("result: {}", response);
	}

}
