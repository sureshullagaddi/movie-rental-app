package com.etraveligroup.movie.rental.exceptions;

/**
 * Exception thrown when there is an error parsing an invoice.
 * This could be due to invalid data format, missing fields, or other parsing issues.
 */

public class RentalsNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new RentalsNotFoundException with the specified message.
     *
     * @param message the detail message
     */
    public RentalsNotFoundException(String message) {
        super(message);
    }
}
