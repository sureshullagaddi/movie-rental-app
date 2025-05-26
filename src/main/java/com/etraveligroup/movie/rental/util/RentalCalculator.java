package com.etraveligroup.movie.rental.util;

import com.etraveligroup.movie.rental.enums.MovieType;

import java.math.BigDecimal;

/**
 * Rental calculator
 */
public final class RentalCalculator {


    /**
     * Rental calculator private constructor to prevent instantiation
     */
    private RentalCalculator() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Calculates the rent based in movie type
     *
     * @param movieType movieType
     * @param daysRented day rented
     */
    public static BigDecimal calculateAmount(MovieType movieType, int daysRented) {

        if (movieType == null) {
            throw new IllegalArgumentException("Movie type must not be null");
        }
        return movieType.calculateAmount(daysRented);
    }
}