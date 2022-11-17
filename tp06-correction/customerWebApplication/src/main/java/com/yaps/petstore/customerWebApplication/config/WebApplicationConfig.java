package com.yaps.petstore.customerWebApplication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yaps.common.dao.ConnectionManager;
import com.yaps.common.dao.ConnectionManagerImpl;
import com.yaps.common.dao.IdSequenceDAO;
import com.yaps.common.dao.IdSequenceDAOImpl;

@Configuration
public class WebApplicationConfig {
    @Value("${yaps.url}")
    String url;

    @Value("${yaps.user}")
    String user;

    @Value("${yaps.password}")
    String password;

    @Bean(destroyMethod = "")
    public ConnectionManager connectionManager() {
        return new ConnectionManagerImpl(url, user, password);
    }

    @Bean
    IdSequenceDAO idSequenceDAO() {
        return new IdSequenceDAOImpl(connectionManager());
    }

}
