package com.etraveligroup.movie.rental.exceptions;

import java.io.Serial;
import java.util.List;

/**
 * Exception thrown when there are errors during rental processing.
 */
public class RentalProcessingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<String> errors;

    /**
     * Constructs a new RentalProcessingException with the specified message.
     * @param message
     * @param errors
     */
    public RentalProcessingException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }
    public List<String> getErrors() {
        return errors;
    }
}
