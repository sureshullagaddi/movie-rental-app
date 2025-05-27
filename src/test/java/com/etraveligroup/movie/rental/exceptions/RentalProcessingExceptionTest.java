package com.etraveligroup.movie.rental.exceptions;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RentalProcessingExceptionTest {

    @Test
    void testMessageAndErrors() {
        String message = "Rental processing failed";
        List<String> errors = Arrays.asList("Error 1", "Error 2");
        RentalProcessingException ex = new RentalProcessingException(message, errors);

        assertEquals(message, ex.getMessage());
        assertEquals(errors, ex.getErrors());
    }

    @Test
    void testErrorsListIsSameReference() {
        List<String> errors = Arrays.asList("E1");
        RentalProcessingException ex = new RentalProcessingException("msg", errors);
        assertSame(errors, ex.getErrors());
    }

    @Test
    void testIsRuntimeException() {
        RentalProcessingException ex = new RentalProcessingException("msg", Arrays.asList());
        assertTrue(ex instanceof RuntimeException);
    }
}
