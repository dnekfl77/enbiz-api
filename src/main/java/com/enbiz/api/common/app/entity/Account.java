package com.enbiz.api.common.app.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;

import com.enbiz.api.common.app.enums.SocialType;
import com.enbiz.api.common.app.service.social.SocialService.SocialUser;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class Account implements Serializable {
	private static final long serialVersionUID = -8816348912911770708L;

	private Long id;
	private String username;
	private String passwd;
	private String name;
	private String phoneNo;
	private String email;
	private SocialType socialType;
	private String socialToken;

	private LocalDateTime createDt;
	private LocalDateTime updateDt;
	
	public static Account from(SocialUser socialUser) {
		return Account.builder()
				.username(StringUtils.join(StringUtils.substringBeforeLast(socialUser.getEmail(), "@"), "@"))
				.passwd(StringUtils.EMPTY)
				.name(socialUser.getName())
				.phoneNo(socialUser.getMobileNo().toJoinString("-"))
				.email(socialUser.getEmail())
				.socialType(socialUser.getSocialType())
				.socialToken(socialUser.getId())
				.build();
	}
}
