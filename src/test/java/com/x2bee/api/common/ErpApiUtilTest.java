package com.x2bee.api.common;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.LinkedMultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.x2bee.common.base.rest.Response;
import com.x2bee.common.base.rest.erpapi.ErpApiUtil;
import com.x2bee.common.base.rest.erpapi.ErpRequest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class ErpApiUtilTest {
	@Autowired
	private ErpApiUtil erpApiUtil;
	
	private String testServerDomain = "http://localhost:8081";

	@Test
	void getSampleById() throws Exception {
		LinkedMultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
		headers.add("test-header-name1", "test header value 1 입니다.");
		headers.add("test-header-name2", "test header value 2 입니다.");

		LinkedMultiValueMap<String,String> formData = new LinkedMultiValueMap<>();
		formData.add("inData", "test input data");

		ErpRequest request = new ErpRequest()
				.setIfType("IFTYP_001") 
				.setIfId("IFID_002")
				.setUrl(testServerDomain+ "/theHandsome/erp-ifs")
				.setHeaders(headers)
				.setFormData(formData);
		
		Response<Map<String,Object>> response = erpApiUtil.post(request, new TypeReference<Map<String,Object>>() {});
		
		log.debug("result: {}", response);
	}

}
