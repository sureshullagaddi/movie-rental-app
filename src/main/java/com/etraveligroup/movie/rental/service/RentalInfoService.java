package com.etraveligroup.movie.rental.service;

import com.etraveligroup.movie.rental.dto.CustomerRequestDTO;

import java.util.concurrent.CompletableFuture;

/**
 * Interface for rental  information services
 */
public interface RentalInfoService {

    /**
     * Generates an invoice for the given customer.
     *
     * @param customer the customer for whom to generate the invoice
     * @return the invoice as a string
     */
    CompletableFuture<String> generateInvoice(CustomerRequestDTO customer);
}
