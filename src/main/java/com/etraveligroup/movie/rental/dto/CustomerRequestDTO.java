package com.etraveligroup.movie.rental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * Customer who rents the movie
 * Each customer will have name and list of movie rentals associated with them
 *
 * @param name
 * @param rentals
 */
public record CustomerRequestDTO(@NotBlank String name, @NotEmpty List<MovieRentalDTO> rentals) {
}
