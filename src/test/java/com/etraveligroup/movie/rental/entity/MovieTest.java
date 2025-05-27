package com.etraveligroup.movie.rental.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    @Test
    void testGettersAndSetters() {
        Movie movie = new Movie();
        movie.setId("MOV001");
        movie.setTitle("Inception");

        MoviePricing pricing = new MoviePricing();
        movie.setPricing(pricing);

        assertEquals("MOV001", movie.getId());
        assertEquals("Inception", movie.getTitle());
        assertEquals(pricing, movie.getPricing());
    }

    @Test
    void testToStringAndEquals() {
        Movie movie1 = new Movie();
        movie1.setId("MOV002");
        movie1.setTitle("Interstellar");

        Movie movie2 = new Movie();
        movie2.setId("MOV002");
        movie2.setTitle("Interstellar");

        assertEquals(movie1, movie2);
        assertTrue(movie1.toString().contains("MOV002"));
        assertTrue(movie1.toString().contains("Interstellar"));
    }
}
