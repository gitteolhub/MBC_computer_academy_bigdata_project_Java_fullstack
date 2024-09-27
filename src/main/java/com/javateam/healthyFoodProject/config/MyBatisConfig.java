package com.javateam.healthyFoodProject.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class MyBatisConfig {
	
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")	// properties파일에서 설정을 읽어오기 위한 어노테이션
	public DataSource getDataSource() {
		
		return DataSourceBuilder.create()
								.type(HikariDataSource.class)		// HikariCP를 사용하도록 설정
								.build();
		
	}
	
	@Bean
	public PlatformTransactionManager getTransactionManager() {
		
		return new DataSourceTransactionManager(this.getDataSource());
	}

}
