package com.x2bee.api.common.app.dto.request.member;
import org.apache.ibatis.type.Alias;

import com.x2bee.common.base.entity.BaseCommonEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * ==========================
 * 
 * ==========================
 */

@Alias("Pt2001Request")
@Setter
@Getter
public class Pt2001Request extends BaseCommonEntity {


	private static final long serialVersionUID = 5866118953981336601L;

	private String ci;
	private String custNm;
	private String birthDt;
	private String mophNo;
}
