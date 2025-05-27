package com.etraveligroup.movie.rental.util;

import com.etraveligroup.movie.rental.entity.MoviePricing;

import java.math.BigDecimal;

public final class PriceCalculator {

    private PriceCalculator() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static BigDecimal calculateRentalAmount(MoviePricing pricing, int days) {
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