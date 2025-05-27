package com.etraveligroup.movie.rental.exceptions;

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
