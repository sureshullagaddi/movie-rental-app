package com.etraveligroup.movie.rental.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class InvoiceFormatterTest {

    @Test
    void formatHeader_returnsCorrectHeader() {
        try (MockedStatic<InvoiceFormatter> mocked = mockStatic(InvoiceFormatter.class)) {
            mocked.when(() -> InvoiceFormatter.formatHeader("John")).thenReturn("Mocked Header\n");
            assertEquals("Mocked Header\n", InvoiceFormatter.formatHeader("John"));
        }
    }

    @Test
    void formatLine_returnsCorrectLine() {
        try (MockedStatic<InvoiceFormatter> mocked = mockStatic(InvoiceFormatter.class)) {
            mocked.when(() -> InvoiceFormatter.formatLine("Movie", new BigDecimal("1.23")))
                    .thenReturn("Mocked Line\n");
            assertEquals("Mocked Line\n", InvoiceFormatter.formatLine("Movie", new BigDecimal("1.23")));
        }
    }

    @Test
    void formatFooter_returnsCorrectFooter() {
        try (MockedStatic<InvoiceFormatter> mocked = mockStatic(InvoiceFormatter.class)) {
            mocked.when(() -> InvoiceFormatter.formatFooter(new BigDecimal("5.50"), 2))
                    .thenReturn("Mocked Footer\n");
            assertEquals("Mocked Footer\n", InvoiceFormatter.formatFooter(new BigDecimal("5.50"), 2));
        }
    }
}