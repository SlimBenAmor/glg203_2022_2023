package com.yaps.petstore.customerApplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yaps.common.ui.SimpleIOImpl;
import com.yaps.common.ui.SimpleIOInterface;

/**
 * Configuration pour l'interface utilisateur.
 * Les classes d'entrées/sorties n'étant pas dans l'application elle-même (mais dans com.yaps.common.ui),
 * on va utiliser des Beans explicites pour les créer.
 */
@Configuration
public class UIConfig {
    
    @Bean
    SimpleIOInterface simpleIO() {
        return new SimpleIOImpl();
    }
}
