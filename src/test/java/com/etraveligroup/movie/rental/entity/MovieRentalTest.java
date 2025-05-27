package com.etraveligroup.movie.rental.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MovieRentalTest {

    @Test
    void testGettersAndSetters() {
        MovieRental rental = new MovieRental();
        rental.setId(100L);
        rental.setDays(7);

        Customer customer = new Customer();
        customer.setId(1L);
        rental.setCustomer(customer);

        Movie movie = new Movie();
        movie.setId("MOV001");
        rental.setMovie(movie);

        assertEquals(100L, rental.getId());
        assertEquals(7, rental.getDays());
        assertEquals(customer, rental.getCustomer());
        assertEquals(movie, rental.getMovie());
    }

    @Test
    void testToStringExcludesCustomer() {
        MovieRental rental = new MovieRental();
        rental.setId(101L);
        rental.setDays(3);
        rental.setCustomer(new Customer());
        rental.setMovie(new Movie());

        String str = rental.toString();
        assertTrue(str.contains("id=101"));
        assertTrue(str.contains("days=3"));
        assertFalse(str.contains("customer"));
    }

    @Test
    void testEqualsAndHashCode() {
        MovieRental r1 = new MovieRental();
        r1.setId(200L);
        r1.setDays(5);

        MovieRental r2 = new MovieRental();
        r2.setId(200L);
        r2.setDays(5);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }
}
