package com.enbiz.api.common.app.entity.criteria;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCriteria implements Serializable {
	private static final long serialVersionUID = 3367217500766146425L;

	private String socialToken;
	private String username;
	private Long id;
}
