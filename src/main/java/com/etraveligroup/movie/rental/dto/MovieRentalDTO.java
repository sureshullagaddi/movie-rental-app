package com.etraveligroup.movie.rental.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


/**
 * Data Transfer Object for Movie Rental.
 * This record encapsulates the details of a movie rental, including the movie ID and the number of days for which the movie is rented.
 *
 * @param movieId the unique identifier of the movie being rented
 * @param days    the number of days for which the movie is rented, must be at least 1
 * @author Suresh
 * @version 1.0
 * @since 1.0
 */
public record MovieRentalDTO(@NotBlank String movieId, @Min(1) int days) {
}
