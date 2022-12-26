package com.x2bee.api.common.app.dto.response.sample;

import org.apache.ibatis.type.Alias;

import com.x2bee.common.base.masking.MaskString;
import com.x2bee.common.base.masking.MaskingType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Alias("sampleResponse")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SampleResponse {
	private Long id;
	private String name;
	@MaskString(type = MaskingType.NAME_KR)
	private String nameMasking;
	private String description;
}
