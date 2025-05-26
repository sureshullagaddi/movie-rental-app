package com.etraveligroup.movie.rental.constants;

import java.math.BigDecimal;

/**
 * Rental app constants
 */
public final class MovieRentalConstants {

    private MovieRentalConstants() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    // REGULAR movie
    public static final BigDecimal BASE_AMOUNT_REGULAR = BigDecimal.valueOf(2);
    public static final int REGULAR_DAYS_THRESHOLD = 2;
    public static final BigDecimal EXTRA_RATE_REGULAR = BigDecimal.valueOf(1.5);

    // NEW movie
    public static final BigDecimal RATE_NEW = BigDecimal.valueOf(3);

    // CHILDREN movie
    public static final BigDecimal BASE_AMOUNT_CHILDREN = BigDecimal.valueOf(1.5);
    public static final int CHILDREN_DAYS_THRESHOLD = 3;
    public static final BigDecimal EXTRA_RATE_CHILDREN = BigDecimal.valueOf(1.5);

    // Rental processing error message
    public static final String RENTAL_PROCESSING_ERROR = "\tError processing rental\t";
}