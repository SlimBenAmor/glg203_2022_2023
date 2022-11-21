package com.yaps.petstore.customerApplication.config;

import com.yaps.common.dao.ConnectionManager;
import com.yaps.common.dao.ConnectionManagerImpl;
import com.yaps.common.dao.IdSequenceDAO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.yaps.common.dao.IdSequenceDAOImpl;

@Configuration
// @ComponentScan(basePackages = {"com.yaps.petstore.customerApplication"})
public class DAOConfig {

    // Un exemple d'injection de données
    @Value("${tp05.url}")
    String url;
    @Value("${tp05.user}")
    String user;
    @Value("${tp05.password}")
    String password;

    
    //... définitions de beans liés à la base de données ?

    // Conseil : jetez un œil sur com.yaps.petstore.config.DBUnitConfig.java dans les tests.
    // les classes et les objets sont différents, mais les principes utilisés devraient vous être utiles.

    @Bean()
    public ConnectionManager connectionManager() {
        return new ConnectionManagerImpl(url,user,password);
    }
    
    @Bean(destroyMethod = "")
    public IdSequenceDAO idSequenceDAO() {
        return new IdSequenceDAOImpl(connectionManager());
    }

}
