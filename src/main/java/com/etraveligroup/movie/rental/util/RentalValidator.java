package com.etraveligroup.movie.rental.util;

import com.etraveligroup.movie.rental.dto.MovieRentalDTO;

public final class RentalValidator {

    private RentalValidator() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static void validateRental(MovieRentalDTO rental) {
        if (rental == null) {
            throw new IllegalArgumentException("Rental must not be null");
        }
        if (rental.movieId() == null || rental.movieId().isBlank()) {
            throw new IllegalArgumentException(
                    String.format("Invalid movie ID: '%s' in rental: %s", rental.movieId(), rental)
            );
        }
        if (rental.days() < 0) {
            throw new IllegalArgumentException(
                    String.format("Rental days must be non-negative: %d in rental: %s", rental.days(), rental)
            );
        }
    }
}