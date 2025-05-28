package com.etraveligroup.movie.rental.util;

import com.etraveligroup.movie.rental.entity.MoviePricing;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Utility class for calculating the rental amount based on pricing and rental days.
 */
@Slf4j
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
        log.debug("Calculating rental amount: pricing={}, days={}", pricing, days);

        if (Objects.isNull(pricing)) {
            log.error("Pricing must not be null");
            throw new IllegalArgumentException("Pricing must not be null");
        }
        if (days <= 0) {
            log.error("Days must be positive, got: {}", days);
            throw new IllegalArgumentException("Days must be positive");
        }

        int baseDays = pricing.getBaseDays();
        BigDecimal basePrice = pricing.getBasePrice();
        BigDecimal extraPricePerDay = pricing.getExtraPricePerDay();

        log.debug("Base days: {}, Base price: {}, Extra price per day: {}", baseDays, basePrice, extraPricePerDay);

        if (days <= baseDays) {
            log.debug("Days ({}) <= baseDays ({}), returning base price: {}", days, baseDays, basePrice);
            return basePrice;
        }
        int extraDays = days - baseDays;
        BigDecimal total = basePrice.add(extraPricePerDay.multiply(BigDecimal.valueOf(extraDays)));
        log.debug("Extra days: {}, Total amount: {}", extraDays, total);
        return total;
    }
}