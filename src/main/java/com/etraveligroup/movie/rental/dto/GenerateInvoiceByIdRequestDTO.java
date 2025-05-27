package com.etraveligroup.movie.rental.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for generating an invoice by customer ID.
 * This record contains the customer ID which is required to generate the invoice.
 *
 * @param customerId the ID of the customer for whom the invoice is to be generated
 * @author Suresh
 * @version 1.0
 * @since 1.0
 */
public record GenerateInvoiceByIdRequestDTO(
        @NotNull(message = "Customer ID must not be null")
        Long customerId
) {}