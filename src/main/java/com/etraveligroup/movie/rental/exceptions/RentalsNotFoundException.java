package com.etraveligroup.movie.rental.exceptions;

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
