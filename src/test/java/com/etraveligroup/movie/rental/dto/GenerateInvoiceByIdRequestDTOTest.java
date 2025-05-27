package com.etraveligroup.movie.rental.dto;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenerateInvoiceByIdRequestDTOTest {

    @Test
    void testConstructorAndAccessor() {
        GenerateInvoiceByIdRequestDTO dto = new GenerateInvoiceByIdRequestDTO(123L);
        assertEquals(123L, dto.customerId());
    }

    @Test
    void testEqualsAndHashCode() {
        GenerateInvoiceByIdRequestDTO dto1 = new GenerateInvoiceByIdRequestDTO(456L);
        GenerateInvoiceByIdRequestDTO dto2 = new GenerateInvoiceByIdRequestDTO(456L);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GenerateInvoiceByIdRequestDTO dto = new GenerateInvoiceByIdRequestDTO(789L);
        String str = dto.toString();
        assertTrue(str.contains("789"));
        assertTrue(str.contains("GenerateInvoiceByIdRequestDTO"));
    }
}