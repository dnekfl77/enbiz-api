package com.enbiz.api.common.base.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.enbiz.common.base.context.ApplicationContextWrapper;

@Configuration
public class BeanConfig implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;
    
	@Override
	public void afterPropertiesSet() throws Exception {
		ApplicationContextWrapper.setApplicationContext(applicationContext);
	}

}

