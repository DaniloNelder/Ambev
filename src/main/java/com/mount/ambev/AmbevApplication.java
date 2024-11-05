package com.mount.ambev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.mount.ambev.repository")
@SpringBootApplication
@EnableCaching
public class AmbevApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmbevApplication.class, args);	
	}

}
