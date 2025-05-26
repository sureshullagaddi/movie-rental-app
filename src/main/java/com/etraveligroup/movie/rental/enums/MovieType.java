package com.etraveligroup.movie.rental.enums;

import java.math.BigDecimal;

import static com.etraveligroup.movie.rental.constants.MovieRentalConstants.*;

/**
 * Enum representing movie types with pricing strategies.
 * Strategy pattern via enum constants.
 * Uses BigDecimal for precision calculations.
 * Easily extendable for new movie types.
 */
public enum MovieType {

    /**
     * Regular movie pricing strategy.
     */
    REGULAR {
        @Override
        public BigDecimal calculateAmount(int daysRented) {
            validateDaysRented(daysRented);
            BigDecimal amount = BASE_AMOUNT_REGULAR;
            if (daysRented > REGULAR_DAYS_THRESHOLD) {
                amount = amount.add(
                        BigDecimal.valueOf((long) daysRented - REGULAR_DAYS_THRESHOLD).multiply(EXTRA_RATE_REGULAR)
                );
            }
            return amount;
        }
    },

    /**
     * New release movie pricing strategy.
     */
    NEW {
        @Override
        public BigDecimal calculateAmount(int daysRented) {
            validateDaysRented(daysRented);
            return BigDecimal.valueOf(daysRented).multiply(RATE_NEW);
        }
    },

    /**
     * Children movie pricing strategy.
     */
    CHILDREN {
        @Override
        public BigDecimal calculateAmount(int daysRented) {
            validateDaysRented(daysRented);
            BigDecimal amount = BASE_AMOUNT_CHILDREN;
            if (daysRented > CHILDREN_DAYS_THRESHOLD) {
                amount = amount.add(
                        BigDecimal.valueOf((long) daysRented - CHILDREN_DAYS_THRESHOLD).multiply(EXTRA_RATE_CHILDREN)
                );
            }
            return amount;
        }
    };

    /**
     * Calculates the rental amount for the given days.
     *
     * @param daysRented number of days the movie is rented
     * @return calculated amount
     * @throws IllegalArgumentException if daysRented is negative
     */
    public abstract BigDecimal calculateAmount(int daysRented);

    private static void validateDaysRented(int daysRented) {
        if (daysRented < 0) {
            throw new IllegalArgumentException("daysRented cannot be negative");
        }
    }
}
