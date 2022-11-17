package com.yaps.petstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.yaps.common.dao.IdSequenceDAO;
import com.yaps.common.dao.IdSequenceDAOImpl;

@Configuration
public class DBConfig {
    
    @Bean
    public IdSequenceDAO idSequenceDAO(JdbcTemplate jdbcTemplate) {
        return new IdSequenceDAOImpl(jdbcTemplate);
    }
}
