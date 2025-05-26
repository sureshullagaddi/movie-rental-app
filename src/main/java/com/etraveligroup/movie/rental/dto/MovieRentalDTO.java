package com.etraveligroup.movie.rental.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * @param movieId
 * @param days
 */
public record MovieRentalDTO(@NotBlank String movieId, @Min(1) int days) {
}
