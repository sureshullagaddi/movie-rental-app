package com.etraveligroup.movie.rental.exceptions;

/**
 * Exception thrown when there is an error parsing an invoice.
 * This could be due to invalid data format, missing fields, or other parsing issues.
 */
public class CustomerNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new CustomerNotFoundException with the specified customer ID.
     *
     * @param customerId the ID of the customer that was not found
     */
    public CustomerNotFoundException(String customerId) {
        super(customerId);
    }
}
