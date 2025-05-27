package com.etraveligroup.movie.rental.constants;

import java.math.BigDecimal;

/**
 * Rental app constants
 */
public final class MovieRentalConstants {

    private MovieRentalConstants() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }
    // Constants for invoice formatting
    public static final String NEW = "new";
    public static final int DAYS_RENTED_2 = 2;
    public static final int DAYS_RENTED_1 = 1;

    public static final String PREFIX_CUSTOMER = "Rental Record for ";
    public static final String PREFIX_TOTAL = "Amount owed is";
    public static final String PREFIX_POINTS = "You earned";

}