package com.x2bee.api.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.x2bee.api","com.x2bee.common"})
public class X2beeApiCommonApplication {

	public static void main(String[] args) {
		SpringApplication.run(X2beeApiCommonApplication.class, args);
	}

}
