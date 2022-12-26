package com.x2bee.api.common.base.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.x2bee.common.base.context.ApplicationContextWrapper;

@Configuration
public class BeanConfig implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;
    
	@Override
	public void afterPropertiesSet() throws Exception {
		ApplicationContextWrapper.setApplicationContext(applicationContext);
	}

}

