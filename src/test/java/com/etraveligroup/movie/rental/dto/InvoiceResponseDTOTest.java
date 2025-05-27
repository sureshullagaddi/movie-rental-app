package com.etraveligroup.movie.rental.dto;


import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InvoiceResponseDTOTest {

    @Test
    void testConstructorAndAccessors() {
        InvoiceItemDTO item1 = new InvoiceItemDTO("Movie 1", new BigDecimal("10.00"));
        InvoiceItemDTO item2 = new InvoiceItemDTO("Movie 2", new BigDecimal("20.00"));
        List<InvoiceItemDTO> items = List.of(item1, item2);

        InvoiceResponseDTO dto = new InvoiceResponseDTO("John Doe", items, new BigDecimal("30.00"), 5);

        assertEquals("John Doe", dto.customer());
        assertEquals(items, dto.items());
        assertEquals(new BigDecimal("30.00"), dto.total());
        assertEquals(5, dto.frequentPoints());
    }

    @Test
    void testEqualsAndHashCode() {
        List<InvoiceItemDTO> items = List.of(
                new InvoiceItemDTO("A", new BigDecimal("1.00"))
        );
        InvoiceResponseDTO dto1 = new InvoiceResponseDTO("Alice", items, new BigDecimal("1.00"), 2);
        InvoiceResponseDTO dto2 = new InvoiceResponseDTO("Alice", items, new BigDecimal("1.00"), 2);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        List<InvoiceItemDTO> items = List.of(
                new InvoiceItemDTO("B", new BigDecimal("2.00"))
        );
        InvoiceResponseDTO dto = new InvoiceResponseDTO("Bob", items, new BigDecimal("2.00"), 3);

        String str = dto.toString();
        assertTrue(str.contains("Bob"));
        assertTrue(str.contains("2.00"));
        assertTrue(str.contains("3"));
    }
}
