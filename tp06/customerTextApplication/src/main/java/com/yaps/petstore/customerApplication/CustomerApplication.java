package com.yaps.petstore.customerApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.yaps.petstore.customerApplication.ui.CustomerUI;

@SpringBootApplication
public class CustomerApplication implements CommandLineRunner {

	@Autowired
	CustomerUI customerUI;

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		customerUI.run();
	}
}