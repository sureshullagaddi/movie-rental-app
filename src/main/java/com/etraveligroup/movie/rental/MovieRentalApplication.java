package com.etraveligroup.movie.rental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Main application class for the Movie Rental application.
 * This class is responsible for bootstrapping the Spring Boot application.
 */
@EnableRetry
@SpringBootApplication
public class MovieRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieRentalApplication.class, args);
	}

}
