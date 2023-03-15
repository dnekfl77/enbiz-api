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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author choiyh44
 * @version 1.0
 * @since 2021. 9. 8.
 *
 */
@Configuration
@MapperScan(value="com.enbiz.api.common.app.repository.replica", sqlSessionFactoryRef="replicaDbSqlSessionFactory")
public class ReplicaDbDatabaseConfig {
	
    @Bean(name = "replicaDbDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.replica")
    public DataSource replicaDbDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "replicaDbSqlSessionFactory")
    public SqlSessionFactory replicaDbSqlSessionFactory(@Qualifier("replicaDbDataSource") DataSource replicaDbDataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(replicaDbDataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.enbiz.api.common.app");
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/replica/**/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:common/mapper/mybatis-config.xml"));
        return sqlSessionFactoryBean.getObject();
    }
 
    @Bean(name = "replicaDbSqlSessionTemplate")
    public SqlSessionTemplate replicaDbSqlSessionTemplate(SqlSessionFactory replicaDbSqlSessionFactory) throws Exception { 
        return new SqlSessionTemplate(replicaDbSqlSessionFactory);
    }

    @Bean(name="replicaDbTxManager")
    public PlatformTransactionManager db1TxManager(@Autowired @Qualifier("replicaDbDataSource") DataSource replicaDbDataSource) {
        return new DataSourceTransactionManager(replicaDbDataSource);
    }

}
