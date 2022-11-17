package com.yaps.petstore.config;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yaps.common.dao.ConnectionManager;
import com.yaps.common.dao.ConnectionManagerImpl;
import com.yaps.common.dao.IdSequenceDAO;
import com.yaps.common.dao.IdSequenceDAOImpl;

// Code de configuration de la base de données pour les tests.
// noter que ce code est un peu redondant (certaines parties se retrouvent aussi dans l'application).
@Configuration
public class TestDBConfig {
    
    @Value("${yaps.driverName}")
    String jdbcDriverName;
    @Value("${yaps.url}")
    String url;
    @Value("${yaps.user}")
    String user;
    @Value("${yaps.password}")
    String password;

    @Bean()
    public IDatabaseTester iDatabaseTester() throws ClassNotFoundException {
        return new JdbcDatabaseTester(
            jdbcDriverName,
            url,
            user,
            password);
    }

    @Bean(destroyMethod = "") // prevents spurious close.
    public ConnectionManager connectionManager() {
        return new ConnectionManagerImpl(url, user, password);        
    }

    @Bean
    public IdSequenceDAO iSequenceDAO() {
        return new IdSequenceDAOImpl(connectionManager());
    }
}
