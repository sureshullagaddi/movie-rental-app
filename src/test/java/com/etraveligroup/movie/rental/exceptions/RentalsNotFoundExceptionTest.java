package com.etraveligroup.movie.rental.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RentalsNotFoundExceptionTest {

    @Test
    void testMessageConstructor() {
        String message = "No rentals found";
        RentalsNotFoundException ex = new RentalsNotFoundException(message);
        assertEquals(message, ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testIsRuntimeException() {
        RentalsNotFoundException ex = new RentalsNotFoundException("Test");
        assertTrue(ex instanceof RuntimeException);
    }
}
