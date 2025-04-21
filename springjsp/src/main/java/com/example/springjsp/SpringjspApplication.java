package com.example.springjsp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example.springjsp")
public class SpringjspApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringjspApplication.class, args);
	}

}
