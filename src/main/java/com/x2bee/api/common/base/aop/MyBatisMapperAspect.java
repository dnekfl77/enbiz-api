package com.x2bee.api.common.base.aop;

import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.x2bee.common.base.rest.ClientInfoPopulaterSupport;

@Aspect
@Component
public class MyBatisMapperAspect extends ClientInfoPopulaterSupport {

	@Around("@within(org.apache.ibatis.annotations.Mapper) || @within(org.springframework.stereotype.Repository)")
	public Object process(final ProceedingJoinPoint joinPoint) throws Throwable {
		
		preHandle(joinPoint);
		
		Object result = joinPoint.proceed();
		
		postHandle(result);
		
		return result;
	}

	private void preHandle(final ProceedingJoinPoint joinPoint) {
		configureModelByRequiredValue( joinPoint.getArgs() );
	}
	
	private void postHandle(final Object object) {
		populateClientInfo( object );
	}
	
	private void configureModelByRequiredValue(Object[] args) {
		if ( Objects.nonNull(args) && args.length > 0 ) {
			for (int i = 0; i < args.length; i++) {
				populateClientInfo(args[i]);
			}
		}
	}
	
}
