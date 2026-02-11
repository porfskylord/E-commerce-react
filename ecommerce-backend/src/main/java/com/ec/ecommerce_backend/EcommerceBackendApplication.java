package com.ec.ecommerce_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.ec.ecommerce_backend.repository")
public class EcommerceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceBackendApplication.class, args);
	}

}
