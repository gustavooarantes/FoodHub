package com.gustavooarantes.foodhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FoodhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodhubApplication.class, args);
	}

}
