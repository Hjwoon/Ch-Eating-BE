package com.ch_eatimg.ch_eating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChEatingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChEatingApplication.class, args);
	}

}
