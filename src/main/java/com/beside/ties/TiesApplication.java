package com.beside.ties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiesApplication.class, args);
	}

}
