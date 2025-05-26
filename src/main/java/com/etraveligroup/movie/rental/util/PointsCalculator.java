package com.etraveligroup.movie.rental.util;

import com.etraveligroup.movie.rental.enums.MovieType;


public final class PointsCalculator {

    private PointsCalculator() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static int calculateFrequentRenterPoints(MovieType movieType, int daysRented) {
        if (movieType == null) {
            throw new IllegalArgumentException("Movie type must not be null");
        }
        return (movieType == MovieType.NEW && daysRented > 2) ? 2 : 1;
    }
}