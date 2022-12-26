package com.x2bee.api.common.app.dto.request.sample;

import org.apache.ibatis.type.Alias;

import com.x2bee.common.base.entity.BaseCommonEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("sampleRequest")
@Getter
@Setter
@ToString
public class SampleRequest extends BaseCommonEntity {
	private static final long serialVersionUID = -5756700830219562201L;
	private Long id;
	private String name;
	private String description;
	private String mbrNo;
}
