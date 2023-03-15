package com.enbiz.api.common.app.dto.response.sample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SampleResponse {
	private Long id;
	private String name;
	private String description;
}
