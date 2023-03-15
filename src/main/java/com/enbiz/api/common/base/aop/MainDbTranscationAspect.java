/**
 * 
 */
package com.enbiz.api.common.base.aop;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * @author choiyh44
 * @version 1.0
 * @since 2021. 11. 25.
 *
 */
@Aspect
@Configuration
public class MainDbTranscationAspect {

    @Resource(name="mainDbTxManager")
    private TransactionManager mainDbTxManager;

    private static final String EXPRESSION 
            = "execution(* com.enbiz.api..app.service..*ServiceImpl.register*(..)) "
            + " || execution(* com.enbiz.api..app.service..*ServiceImpl.modify*(..))"
            + " || execution(* com.enbiz.api..app.service..*ServiceImpl.save*(..))"
            + " || execution(* com.enbiz.api..app.service..*ServiceImpl.delete*(..))"
            ;

    @Bean
    public TransactionInterceptor mainDbTransactionAdvice() {

        List<RollbackRuleAttribute> rollbackRules = Collections.singletonList(new RollbackRuleAttribute(Exception.class));

        RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
        transactionAttribute.setRollbackRules(rollbackRules);
        transactionAttribute.setName("*");

        MatchAlwaysTransactionAttributeSource attributeSource = new MatchAlwaysTransactionAttributeSource();
        attributeSource.setTransactionAttribute(transactionAttribute);

        return new TransactionInterceptor(mainDbTxManager, attributeSource);
    }

    @Bean
    public Advisor mainDbTransactionAdvisor() {

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(EXPRESSION);

        return new DefaultPointcutAdvisor(pointcut, mainDbTransactionAdvice());
    }

}
