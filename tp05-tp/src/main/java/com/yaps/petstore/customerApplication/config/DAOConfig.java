package com.yaps.petstore.customerApplication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DAOConfig {

    // Un exemple d'injection de données
    @Value("${tp05.url}")
    String url;

    //... définitions de beans liés à la base de données ?

    // Conseil : jetez un œil sur com.yaps.petstore.config.DBUnitConfig.java dans les tests.
    // les classes et les objets sont différents, mais les principes utilisés devraient vous être utiles.

}
