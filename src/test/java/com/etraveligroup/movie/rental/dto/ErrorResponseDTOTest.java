package com.etraveligroup.movie.rental.dto;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseDTOTest {

    @Test
    void testConstructorAndAccessors() {
        ErrorResponseDTO dto = new ErrorResponseDTO(400, "Bad Request", "Invalid input");
        assertEquals(400, dto.status());
        assertEquals("Bad Request", dto.error());
        assertEquals("Invalid input", dto.message());
    }

    @Test
    void testEqualsAndHashCode() {
        ErrorResponseDTO dto1 = new ErrorResponseDTO(500, "Internal Error", "Server failed");
        ErrorResponseDTO dto2 = new ErrorResponseDTO(500, "Internal Error", "Server failed");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ErrorResponseDTO dto = new ErrorResponseDTO(404, "Not Found", "Resource missing");
        String str = dto.toString();
        assertTrue(str.contains("404"));
        assertTrue(str.contains("Not Found"));
        assertTrue(str.contains("Resource missing"));
    }
}