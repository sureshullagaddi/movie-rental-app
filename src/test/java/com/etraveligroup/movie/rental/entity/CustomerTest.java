package com.etraveligroup.movie.rental.entity;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testGettersAndSetters() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        assertEquals(1L, customer.getId());
        assertEquals("John Doe", customer.getName());
    }

    @Test
    void testRentals() {
        Customer customer = new Customer();
        MovieRental rental = new MovieRental();
        customer.setRentals(List.of(rental));

        assertNotNull(customer.getRentals());
        assertEquals(1, customer.getRentals().size());
    }

    @Test
    void testToStringDoesNotIncludeRentals() {
        Customer customer = new Customer();
        customer.setId(2L);
        customer.setName("Jane Doe");
        customer.setRentals(List.of());

        String str = customer.toString();
        assertTrue(str.contains("id=2"));
        assertTrue(str.contains("name=Jane Doe"));
        assertFalse(str.contains("rentals"));
    }
}
