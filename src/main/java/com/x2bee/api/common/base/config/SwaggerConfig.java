package com.x2bee.api.common.base.config;

import java.sql.Timestamp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.basePackage("com.x2bee.api.common.app.controller"))
			.paths(PathSelectors.ant("/api/**"))
			.build()
			.directModelSubstitute(Timestamp.class, Long.class)
			.apiInfo(apiEndPointsInfo());
	}
	
	private ApiInfo apiEndPointsInfo() {
		return new ApiInfoBuilder().title("TheHandsome API")
				.description("TheHandsome REST API 설명서입니다.")
				.contact(new Contact("한섬", "http://thehandsome.com/", null))
				.version("v1")
				.build();
	}

}