package com.etraveligroup.movie.rental.util;

import com.etraveligroup.movie.rental.dto.MovieRentalDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RentalValidator {

    private RentalValidator() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static void validateRental(MovieRentalDTO rental) {
        log.debug("Validating rental: {}", rental);
        if (rental == null) {
            log.error("Rental must not be null");
            throw new IllegalArgumentException("Rental must not be null");
        }
        if (rental.movieId() == null || rental.movieId().isBlank()) {
            log.error("Invalid movie ID: '{}' in rental: {}", rental.movieId(), rental);
            throw new IllegalArgumentException(
                    String.format("Invalid movie ID: '%s' in rental: %s", rental.movieId(), rental)
            );
        }
        if (rental.days() < 0) {
            log.error("Rental days must be non-negative: {} in rental: {}", rental.days(), rental);
            throw new IllegalArgumentException(
                    String.format("Rental days must be non-negative: %d in rental: %s", rental.days(), rental)
            );
        }
        log.debug("Rental validation passed for: {}", rental);
    }
}