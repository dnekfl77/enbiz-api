package com.enbiz.api.common.app.service.certification;

import java.util.Map;

public interface CertificationService {

	Map<String, Object> encodeNiceRequestData(Map<String, Object> params);

	Map<String, Object> decodeNiceResponseData(Map<String, Object> params);

}
