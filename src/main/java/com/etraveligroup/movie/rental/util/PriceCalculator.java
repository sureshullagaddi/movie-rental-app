package com.etraveligroup.movie.rental.util;

import com.etraveligroup.movie.rental.entity.MoviePricing;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Utility class for calculating the rental amount based on pricing and rental days.
 */
public final class PriceCalculator {

    private PriceCalculator() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Calculates the rental amount for a given pricing and number of days.
     *
     * @param pricing the movie pricing details (must not be null)
     * @param days    the number of days rented (must be positive)
     * @return the total rental amount
     * @throws IllegalArgumentException if pricing is null or days is not positive
     */
    public static BigDecimal calculateRentalAmount(MoviePricing pricing, int days) {
        if (Objects.isNull(pricing)) {
            throw new IllegalArgumentException("Pricing must not be null");
        }
        if (days <= 0) {
            throw new IllegalArgumentException("Days must be positive");
        }

        int baseDays = pricing.getBaseDays();
        BigDecimal basePrice = pricing.getBasePrice();
        BigDecimal extraPricePerDay = pricing.getExtraPricePerDay();

        if (days <= baseDays) {
            return basePrice;
        }
        int extraDays = days - baseDays;
        return basePrice.add(extraPricePerDay.multiply(BigDecimal.valueOf(extraDays)));
    }
}