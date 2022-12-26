package com.x2bee.api.common.app.service.member;

import javax.validation.Valid;

import com.x2bee.api.common.app.dto.request.member.Pt2001Request;
import com.x2bee.api.common.app.dto.response.member.MemberInfo3Key;
import com.x2bee.common.base.rest.Response;

public interface MemberRequestService {

	Response<MemberInfo3Key> receiveHPtoHandsomeHsMemberInfo3key(Pt2001Request pt2001Request) throws Exception;

}
