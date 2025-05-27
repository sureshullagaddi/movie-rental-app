package com.etraveligroup.movie.rental.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PdfGenerationExceptionTest {

    @Test
    void testMessageConstructor() {
        String message = "PDF generation failed";
        PdfGenerationException ex = new PdfGenerationException(message);
        assertEquals(message, ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testMessageAndCauseConstructor() {
        String message = "PDF error";
        Throwable cause = new RuntimeException("IO error");
        PdfGenerationException ex = new PdfGenerationException(message, cause);
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void testIsRuntimeException() {
        PdfGenerationException ex = new PdfGenerationException("Test");
        assertTrue(ex instanceof RuntimeException);
    }
}
