package com.x2bee.api.common.app.controller.member;
import javax.validation.Valid;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.x2bee.api.common.app.dto.request.member.Pt2001Request;
import com.x2bee.api.common.app.dto.request.sample.SampleRequest;
import com.x2bee.api.common.app.dto.response.member.MemberInfo3Key;
import com.x2bee.api.common.app.service.interfaces.SampleInfService;
import com.x2bee.api.common.app.service.member.MemberRequestService;
import com.x2bee.common.base.rest.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/member")
@Lazy
@Slf4j
@RequiredArgsConstructor
public class MemberRequestController {
	private final MemberRequestService mbrRequestService;
	
	@PostMapping("/hpoint/hsMemberInfo3key.hd")
	@ResponseBody
	public Response<MemberInfo3Key> receiveHPtoHandsomeHsMemberInfo3key(@RequestBody Pt2001Request pt2001Request) throws Exception {
		return mbrRequestService.receiveHPtoHandsomeHsMemberInfo3key(pt2001Request);
	}

}
