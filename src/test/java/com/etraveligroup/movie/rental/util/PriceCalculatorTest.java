package com.etraveligroup.movie.rental.util;

import com.etraveligroup.movie.rental.entity.MoviePricing;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceCalculatorTest {

    @Mock
    MoviePricing pricing;

    static Stream<org.junit.jupiter.params.provider.Arguments> rentalAmountProvider() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(2, BigDecimal.valueOf(10)),
                org.junit.jupiter.params.provider.Arguments.of(3, BigDecimal.valueOf(10)),
                org.junit.jupiter.params.provider.Arguments.of(5, BigDecimal.valueOf(14))
        );
    }

    @ParameterizedTest
    @MethodSource("rentalAmountProvider")
    void calculateRentalAmount_variousDays_returnsExpectedAmount(int days, BigDecimal expected) {
        when(pricing.getBaseDays()).thenReturn(3);
        when(pricing.getBasePrice()).thenReturn(BigDecimal.valueOf(10));
        when(pricing.getExtraPricePerDay()).thenReturn(BigDecimal.valueOf(2));

        BigDecimal result = PriceCalculator.calculateRentalAmount(pricing, days);
        assertEquals(expected, result);
    }

    @Test
    void calculateRentalAmount_nullPricing_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> PriceCalculator.calculateRentalAmount(null, 2));
    }

    @Test
    void calculateRentalAmount_nonPositiveDays_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> PriceCalculator.calculateRentalAmount(pricing, 0));
        assertThrows(IllegalArgumentException.class, () -> PriceCalculator.calculateRentalAmount(pricing, -1));
    }
}