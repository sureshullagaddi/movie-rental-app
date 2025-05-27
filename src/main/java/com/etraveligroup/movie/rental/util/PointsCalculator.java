package com.etraveligroup.movie.rental.util;

import static com.etraveligroup.movie.rental.constants.MovieRentalConstants.*;

public final class PointsCalculator {

    private PointsCalculator() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static int calculateFrequentRenterPoints(String pricingCode, int daysRented) {
        if (pricingCode == null) {
            throw new IllegalArgumentException("Pricing code must not be null");
        }
        return (NEW.equalsIgnoreCase(pricingCode) && daysRented > DAYS_RENTED_2) ? DAYS_RENTED_2 : DAYS_RENTED_1;
    }
}
