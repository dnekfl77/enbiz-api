package com.x2bee.api.common.app.entity;

import org.apache.ibatis.type.Alias;

import com.x2bee.common.base.entity.BaseCommonEntity;

import lombok.Getter;
import lombok.Setter;

@Alias("StInfLog")
@Getter
@Setter
public class StInfLog extends BaseCommonEntity {
	private static final long serialVersionUID = 4303256763616575895L;

	private String infLogSeq;
    private String infTypCd;
    private String reqRplyGbCd;
    private String infId;
    private String reqUrl;
    private String reqMethod;
    private String reqHerVal;
    private String qryStrPrmt;
    private String reqPrmtObj;
    private String uprInfLogSeq;
    private String rplyStatCd;
    private String rplyHerVal;
    private String rplyCont;
}