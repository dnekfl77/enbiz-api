/**
 * 
 */
package com.enbiz.api.common.base.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author choiyh44
 * @version 1.0
 * @since 2021. 9. 8.
 *
 */
@Configuration
@MapperScan(value="com.enbiz.api.common.app.repository.main", sqlSessionFactoryRef="mainDbSqlSessionFactory")
public class MainDbDatabaseConfig {
	
    @Bean(name = "mainDbDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.main")
    public DataSource mainDbDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mainDbSqlSessionFactory")
    @Primary
    public SqlSessionFactory mainDbSqlSessionFactory(@Qualifier("mainDbDataSource") DataSource mainDbDataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(mainDbDataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.enbiz.api.common.app");
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/main/**/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:common/mapper/mybatis-config.xml"));
        return sqlSessionFactoryBean.getObject();
    }
 
    @Bean(name = "mainDbSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate mainDbSqlSessionTemplate(SqlSessionFactory mainDbSqlSessionFactory) throws Exception { 
        return new SqlSessionTemplate(mainDbSqlSessionFactory);
    }

    @Bean(name="mainDbTxManager")
    @Primary
    public PlatformTransactionManager db1TxManager(@Autowired @Qualifier("mainDbDataSource") DataSource mainDbDataSource) {
        return new DataSourceTransactionManager(mainDbDataSource);
    }

}
