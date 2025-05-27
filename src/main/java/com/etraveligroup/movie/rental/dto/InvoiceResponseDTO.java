package com.etraveligroup.movie.rental.dto;

import java.util.List;
import java.math.BigDecimal;
/**
 * Represents the response for an invoice.
 *
 * @param customer       The name of the customer.
 * @param items          The list of items in the invoice.
 * @param total          The total amount for the invoice.
 * @param frequentPoints The number of frequent renter points earned.
 */
public record InvoiceResponseDTO(
        String customer,
        List<InvoiceItemDTO> items,
        BigDecimal total,
        int frequentPoints
) {}
