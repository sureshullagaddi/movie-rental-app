package com.etraveligroup.movie.rental.dto;


import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class InvoiceItemDTOTest {

    @Test
    void testConstructorAndAccessors() {
        InvoiceItemDTO dto = new InvoiceItemDTO("Movie Title", new BigDecimal("19.99"));
        assertEquals("Movie Title", dto.title());
        assertEquals(new BigDecimal("19.99"), dto.price());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceItemDTO dto1 = new InvoiceItemDTO("Book", new BigDecimal("10.00"));
        InvoiceItemDTO dto2 = new InvoiceItemDTO("Book", new BigDecimal("10.00"));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        InvoiceItemDTO dto = new InvoiceItemDTO("Game", new BigDecimal("59.99"));
        String str = dto.toString();
        assertTrue(str.contains("Game"));
        assertTrue(str.contains("59.99"));
    }
}