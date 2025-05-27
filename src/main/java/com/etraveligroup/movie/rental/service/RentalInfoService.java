package com.etraveligroup.movie.rental.service;

import com.etraveligroup.movie.rental.dto.GenerateInvoiceRequestDTO;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

/**
 * Interface for rental  information services
 */
public interface RentalInfoService {

    /**
     * Generates an invoice by customer.
     *
     * @param generateInvoiceRequestDTO the customer for whom to generate the invoice
     * @return the invoice as a string
     */
    Mono<String> generateInvoiceByName(GenerateInvoiceRequestDTO generateInvoiceRequestDTO);
    /**
     * Generates an invoice by customer ID.
     *
     * @param customerId the customer ID for whom to generate the invoice
     * @return the invoice as a string
     */
    Mono<String> generateInvoiceById(Long customerId);
}
