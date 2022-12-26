package com.x2bee.api.common.app.controller;

import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.lang.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/social")
@Lazy
@Slf4j
@RequiredArgsConstructor
public class SocialController {

	@RequestMapping("/sign")
	public Map<String, Object> sign(@RequestParam Map<String, Object> request) {
		log.debug("request: {}", request);

		return Maps.<String, Object>of("name", "you").build();
	}
}
