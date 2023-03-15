package com.enbiz.api.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.enbiz.api","com.enbiz.common"})
public class ApiSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiSampleApplication.class, args);
	}

}
