package com.etraveligroup.movie.rental.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MoviePricingTest {

    @Test
    void testGettersAndSetters() {
        MoviePricing pricing = new MoviePricing();
        pricing.setCode("REGULAR");
        pricing.setBaseDays(2);
        pricing.setBasePrice(new BigDecimal("30.00"));
        pricing.setExtraPricePerDay(new BigDecimal("10.00"));

        assertEquals("REGULAR", pricing.getCode());
        assertEquals(2, pricing.getBaseDays());
        assertEquals(new BigDecimal("30.00"), pricing.getBasePrice());
        assertEquals(new BigDecimal("10.00"), pricing.getExtraPricePerDay());
    }

    @Test
    void testToStringAndEquals() {
        MoviePricing p1 = new MoviePricing();
        p1.setCode("NEW");
        p1.setBaseDays(1);
        p1.setBasePrice(new BigDecimal("40.00"));
        p1.setExtraPricePerDay(new BigDecimal("15.00"));

        MoviePricing p2 = new MoviePricing();
        p2.setCode("NEW");
        p2.setBaseDays(1);
        p2.setBasePrice(new BigDecimal("40.00"));
        p2.setExtraPricePerDay(new BigDecimal("15.00"));

        assertEquals(p1, p2);
        assertTrue(p1.toString().contains("NEW"));
        assertTrue(p1.toString().contains("40.00"));
    }
}
