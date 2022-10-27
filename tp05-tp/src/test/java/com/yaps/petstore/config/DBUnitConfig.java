package com.yaps.petstore.config;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBUnitConfig {
    
    @Value("${tp05.driverName}")
    String jdbcDriverName;
    @Value("${tp05.url}")
    String url;
    @Value("${tp05.user}")
    String user;
    @Value("${tp05.password}")
    String password;

    @Bean()
    public IDatabaseTester iDatabaseTester() throws ClassNotFoundException {
        return new JdbcDatabaseTester(
            jdbcDriverName,
            url,
            user,
            password);
    }

}
