package com.etraveligroup.movie.rental.util;

import com.etraveligroup.movie.rental.enums.MovieType;


public final class PointsCalculator {

    private PointsCalculator() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static int calculateFrequentRenterPoints(String pricingCode, int daysRented) {
        if (pricingCode == null) {
            throw new IllegalArgumentException("Pricing code must not be null");
        }
        return ("new".equalsIgnoreCase(pricingCode) && daysRented > 2) ? 2 : 1;
    }
}
