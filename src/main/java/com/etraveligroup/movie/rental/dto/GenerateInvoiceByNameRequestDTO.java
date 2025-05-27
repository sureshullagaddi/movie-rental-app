package com.etraveligroup.movie.rental.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for generating an invoice by customer name.
 * This record contains the customer name which is required to generate the invoice.
 * * @param customerName the name of the customer for whom the invoice is to be generated
 * * @author Suresh
 * * @version 1.0
 * * @since 1.0
 */
public record GenerateInvoiceByNameRequestDTO(
        @NotBlank(message = "Customer name must not be blank")
        String customerName
) {
}