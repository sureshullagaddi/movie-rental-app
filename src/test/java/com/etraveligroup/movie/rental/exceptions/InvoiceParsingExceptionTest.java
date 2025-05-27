package com.etraveligroup.movie.rental.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvoiceParsingExceptionTest {

    @Test
    void testMessageConstructor() {
        String message = "Invalid invoice format";
        InvoiceParsingException ex = new InvoiceParsingException(message);
        assertEquals(message, ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testMessageAndCauseConstructor() {
        String message = "Missing field";
        Throwable cause = new IllegalArgumentException("Field missing");
        InvoiceParsingException ex = new InvoiceParsingException(message, cause);
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void testIsRuntimeException() {
        InvoiceParsingException ex = new InvoiceParsingException("Test");
        assertTrue(ex instanceof RuntimeException);
    }
}
