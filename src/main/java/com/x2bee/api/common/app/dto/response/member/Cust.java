package com.x2bee.api.common.app.dto.response.member;

import org.apache.ibatis.type.Alias;

import com.x2bee.common.base.entity.BaseCommonEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * ==========================
 * 
 * ==========================
 */

@Alias("Cust")
@Setter
@Getter
public class Cust {
	
	private static final long serialVersionUID = 369165844984769746L;
	
	private String custUniqKey; 		//참여사 고객 key값
	private String custNm; 				//고객명
	private String chId; 				//참여사 기존 온라인 ID
	private String email;				//이메일
	private String addrType; 			//신구조소구분
	private String postNo; 				//우편번호
	private String addr1; 				//기본주소
	private String addr2; 				//상세주소
	private String emailYn;			 	//email 수신여부
	private String smsYn;	 			//sms 수신여부
	private String tmYn; 				//tm 수신여부
	private String dmYn; 				//dm 수신여부
}
