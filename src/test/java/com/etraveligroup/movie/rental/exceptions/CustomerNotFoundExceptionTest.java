package com.etraveligroup.movie.rental.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        String customerId = "123";
        CustomerNotFoundException ex = new CustomerNotFoundException(customerId);
        assertEquals(customerId, ex.getMessage());
    }

    @Test
    void testIsRuntimeException() {
        CustomerNotFoundException ex = new CustomerNotFoundException("456");
        assertTrue(ex instanceof RuntimeException);
    }
}
