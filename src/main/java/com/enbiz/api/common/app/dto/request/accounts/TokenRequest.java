package com.enbiz.api.common.app.dto.request.accounts;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Alias("TokenRequest")
@Getter
@Setter
public class TokenRequest implements Serializable {
	private static final long serialVersionUID = -5919705778164630059L;
	
	@NotEmpty
	private String loginId;
	// @NotEmpty
	private String password;
}
