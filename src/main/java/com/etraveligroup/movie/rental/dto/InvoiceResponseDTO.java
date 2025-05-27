package com.etraveligroup.movie.rental.dto;

import java.util.List;
import java.math.BigDecimal;

/**
 * Data Transfer Object for Invoice Response.
 * This record encapsulates the details of an invoice, including the customer name,
 * * a list of items in the invoice, the total amount, and the frequent points earned.
 * <p>
 * * @param customer the name of the customer associated with the invoice
 * * @param items a list of items included in the invoice, represented as InvoiceItemDTO
 * <p>
 * * @param total the total amount of the invoice
 * * @param frequentPoints the number of frequent points earned from this invoice
 * <p>
 * * @author Suresh
 * * @version 1.0
 * * @since 1.0
 */
public record InvoiceResponseDTO(
        String customer,
        List<InvoiceItemDTO> items,
        BigDecimal total,
        int frequentPoints
) {
}
