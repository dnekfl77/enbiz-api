package com.enbiz.api.common.app.entity;

import javax.validation.constraints.NotNull;

import org.apache.ibatis.type.Alias;

import com.enbiz.common.base.entity.BaseCommonEntity;

import lombok.Getter;
import lombok.Setter;

@Alias("Sample")
@Getter
@Setter
public class Sample extends BaseCommonEntity {
	private static final long serialVersionUID = -5756700830219562201L;
	private Long id;
	@NotNull
	private String name;
	private String description;
}
