package com.etraveligroup.movie.rental.dto;

/**
 * Error response DTO for handling errors in the application.
    * This record encapsulates the HTTP status, error type, and a message describing the error.
    * It is used to provide a structured response when an error occurs, such as in the case of invalid input or server errors.
 * @param status
 * @param error
 * @param message
 */
public record ErrorResponseDTO(
        int status,
        String error,
        String message
) {}

