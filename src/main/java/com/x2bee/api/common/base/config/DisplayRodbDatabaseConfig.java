/**
 * 
 */
package com.x2bee.api.common.base.config;

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
@MapperScan(value="com.x2bee.api.common.app.repository.displayrodb", sqlSessionFactoryRef="displayRodbSqlSessionFactory")
public class DisplayRodbDatabaseConfig {
    @Bean(name = "displayRodbDataSource")
    @ConfigurationProperties(prefix = "spring.displayrodb.datasource")
    public DataSource displayRodbDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "displayRodbSqlSessionFactory")
    public SqlSessionFactory displayRodbSqlSessionFactory(@Qualifier("displayRodbDataSource") DataSource displayRodbDataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(displayRodbDataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.x2bee.api.common.app");
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/displayrodb/**/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:common/mapper/mybatis-config.xml"));
        return sqlSessionFactoryBean.getObject();
    }
 
    @Bean(name = "displayRodbSqlSessionTemplate")
    public SqlSessionTemplate displayRodbSqlSessionTemplate(SqlSessionFactory displayRodbSqlSessionFactory) throws Exception { 
        return new SqlSessionTemplate(displayRodbSqlSessionFactory);
    }

    @Bean(name="displayRodbTxManager")
    public PlatformTransactionManager db1TxManager(@Autowired @Qualifier("displayRodbDataSource") DataSource displayRodbDataSource) {
        return new DataSourceTransactionManager(displayRodbDataSource);
    }

}
