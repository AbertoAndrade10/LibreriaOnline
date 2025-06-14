package com.porfolio.books_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

import com.porfolio.books_microservice.config.CloudinaryProperties;

@EnableCaching
@SpringBootApplication
@EnableConfigurationProperties(CloudinaryProperties.class)
public class BooksMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksMicroserviceApplication.class, args);
	}

}
