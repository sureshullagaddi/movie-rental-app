package com.etraveligroup.movie.rental.util;

import lombok.extern.slf4j.Slf4j;

import static com.etraveligroup.movie.rental.constants.MovieRentalConstants.*;

/**
 * Utility class for calculating frequent renter points based on the movie pricing code and days rented.
 * This class provides a method to determine the number of points earned for a rental.
 *
 * @author Suresh
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public final class PointsCalculator {

    private PointsCalculator() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static int calculateFrequentRenterPoints(String pricingCode, int daysRented) {
        if (pricingCode == null) {
            log.error("Pricing code must not be null");
            throw new IllegalArgumentException("Pricing code must not be null");
        }
        int points = (NEW.equalsIgnoreCase(pricingCode) && daysRented >= DAYS_RENTED_2) ? DAYS_RENTED_2 : DAYS_RENTED_1;
        log.debug("Calculated frequent renter points: pricingCode={}, daysRented={}, points={}", pricingCode, daysRented, points);
        return points;
    }
}