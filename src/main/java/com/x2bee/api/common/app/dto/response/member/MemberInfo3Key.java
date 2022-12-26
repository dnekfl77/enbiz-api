package com.x2bee.api.common.app.dto.response.member;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.x2bee.common.base.entity.BaseCommonEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * ==========================
 * 
 * ==========================
 */

@Alias("MemberInfo3Key")
@Setter
@Getter
public class MemberInfo3Key {

	private static final long serialVersionUID = -3333885803642177056L;
	private String SearchGb; 		//고객조회구분
	private List<Cust> custList;	//참여사 회원목록

}