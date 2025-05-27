package com.etraveligroup.movie.rental.dto;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MovieRentalDTOTest {

    @Test
    void testConstructorAndAccessors() {
        MovieRentalDTO dto = new MovieRentalDTO("MOV123", 5);
        assertEquals("MOV123", dto.movieId());
        assertEquals(5, dto.days());
    }

    @Test
    void testEqualsAndHashCode() {
        MovieRentalDTO dto1 = new MovieRentalDTO("MOV456", 3);
        MovieRentalDTO dto2 = new MovieRentalDTO("MOV456", 3);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MovieRentalDTO dto = new MovieRentalDTO("MOV789", 7);
        String str = dto.toString();
        assertTrue(str.contains("MOV789"));
        assertTrue(str.contains("7"));
    }
}