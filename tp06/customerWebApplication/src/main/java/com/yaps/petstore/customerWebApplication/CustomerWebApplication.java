package com.yaps.petstore.customerWebApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ServletComponentScan // La ligne importante : va faire gérer les servlets par Spring !
@ComponentScan(basePackages = {
	"com.yaps.petstore.customerWebApplication", // Sinon, le fichier de configuration n'est pas trouvé...
	"com.yaps.petstore.customerApplication"	
})
public class CustomerWebApplication  {

	public static void main(String[] args) {
		SpringApplication.run(CustomerWebApplication.class, args);
	}

}